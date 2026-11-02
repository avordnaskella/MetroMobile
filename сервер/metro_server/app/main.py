import logging
import secrets
from contextlib import asynccontextmanager

from fastapi import FastAPI, Request
from fastapi.exceptions import RequestValidationError
from starlette.exceptions import HTTPException as StarletteHTTPException

from . import models  # noqa: F401  (регистрация таблиц)
from .config import settings
from .errors import AppError, app_error_handler, error_response, http_exception_handler, validation_handler
from .routers import auth, defects, reference, shifts
from .seed import init_db, seed_if_empty

log = logging.getLogger("metro")


@asynccontextmanager
async def lifespan(_: FastAPI):
    settings.storage_dir.mkdir(parents=True, exist_ok=True)
    init_db()
    if settings.seed_demo_data:
        seed_if_empty()
    if settings.jwt_secret.startswith("dev-only"):
        log.warning("JWT_SECRET не задан — используется dev-ключ. В продуктиве задайте свой.")
    yield


app = FastAPI(
    title="МетроОсмотр API", version="1.0.0", lifespan=lifespan,
    description="Серверное API приложения «МетроОсмотр». Авторизация: Bearer JWT.",
)
app.add_exception_handler(AppError, app_error_handler)
app.add_exception_handler(RequestValidationError, validation_handler)
app.add_exception_handler(StarletteHTTPException, http_exception_handler)


def _version_tuple(v: str) -> tuple:
    return tuple(int(p) for p in v.strip().split(".")[:3])


@app.middleware("http")
async def request_context(request: Request, call_next):
    """X-Request-Id на каждый ответ, проверка X-App-Version (426), перехват непредвиденных ошибок (500)."""
    rid = "req_" + secrets.token_hex(6)
    request.state.request_id = rid
    ver = request.headers.get("X-App-Version")
    if ver:
        try:
            if _version_tuple(ver) < _version_tuple(settings.min_app_version):
                return error_response(request, 426, "CLIENT_OUTDATED",
                                      "Версия приложения устарела. Обновите приложение")
        except ValueError:
            pass
    try:
        response = await call_next(request)
    except Exception:
        log.exception("Unhandled error, requestId=%s", rid)  # без токенов и тел запросов
        return error_response(request, 500, "INTERNAL_ERROR", "На сервере произошла ошибка. Повторите попытку позже")
    response.headers["X-Request-Id"] = rid
    return response


@app.get("/health", tags=["Service"])
def health():
    return {"status": "ok"}


for r in (auth.router, defects.router, reference.router, shifts.router):
    app.include_router(r, prefix="/api/v1")
