package com.example.metroosmotr.data.repository

import com.example.metroosmotr.data.local.dao.DefectDao
import com.example.metroosmotr.data.mapper.DefectMapper
import com.example.metroosmotr.data.remote.MetroApiService
import com.example.metroosmotr.data.remote.NetworkErrorMapper
import com.example.metroosmotr.domain.model.AppError
import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.repository.DefectReader
import com.example.metroosmotr.domain.repository.DefectSyncSource
import com.example.metroosmotr.domain.repository.NetworkChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefectRepositoryImpl @Inject constructor(
    private val api: MetroApiService,
    private val mapper: DefectMapper,
    private val networkChecker: NetworkChecker,
    private val dao: DefectDao // <-- Добавили DAO
) : DefectReader, DefectSyncSource {

    // --- OFFLINE-FIRST ЧТЕНИЕ ---
    // Всегда отдаем данные из Room. Если сеть есть — Room обновится в фоне.
    override fun observeDefects(): Flow<List<Defect>> {
        return dao.observeAll().map { entities ->
            entities.map { mapper.mapEntityToDomain(it) }
        }
    }

    override suspend fun getDefectById(id: String): Defect? {
        return dao.getById(id)?.let { mapper.mapEntityToDomain(it) }
    }

    // --- СИНХРОНИЗАЦИЯ ---
    override suspend fun fetchFromServer(): List<Defect> {
        if (!networkChecker.isNetworkAvailable()) throw AppError.Network()

        return try {
            val remoteDefects = api.getDefects().items.map { mapper.mapDtoToDomain(it) }
            // Сохраняем в кеш (Room)
            dao.insertAll(remoteDefects.map { mapper.mapDomainToEntity(it).copy(isSynced = true) })
            remoteDefects
        } catch (e: Exception) {
            throw NetworkErrorMapper.map(e)
        }
    }

    override suspend fun pushToServer(defect: Defect): Defect {
        if (!networkChecker.isNetworkAvailable()) throw AppError.Network()

        return try {
            val dto = mapper.mapDomainToDto(defect)
            val responseDto = api.createDefect(dto)
            // После успешной отправки помечаем в БД как синхронизированное
            dao.markAsSynced(defect.id)
            mapper.mapDtoToDomain(responseDto)
        } catch (e: Exception) {
            throw NetworkErrorMapper.map(e)
        }
    }

    override suspend fun getUnsyncedDefects(): List<Defect> {
        return dao.getUnsynced().map { mapper.mapEntityToDomain(it) }
    }

    override suspend fun markAsSynced(id: String) {
        dao.markAsSynced(id)
    }

    // Метод для сохранения локально (когда нет сети)
    suspend fun saveLocal(defect: Defect) {
        dao.insert(mapper.mapDomainToEntity(defect))
    }
}