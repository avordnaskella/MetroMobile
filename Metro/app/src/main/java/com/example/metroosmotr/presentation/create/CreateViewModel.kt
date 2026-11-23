package com.example.metroosmotr.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metroosmotr.domain.model.Defect
import com.example.metroosmotr.domain.model.Severity
import com.example.metroosmotr.domain.repository.FlashlightController
import com.example.metroosmotr.domain.repository.LocationProvider
import com.example.metroosmotr.domain.usecase.CreateDefectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val createDefectUseCase: CreateDefectUseCase,
    private val locationProvider: LocationProvider,
    private val flashlightController: FlashlightController
) : ViewModel() {
    private var currentPhotoUri: String? = null

    // Метод, который вызовет экран после съемки
    fun onPhotoCaptured(uri: String) {
        currentPhotoUri = uri
    }

    private val _uiState = MutableStateFlow(CreateUiState())
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()

    // Заглушки для ID (в реальном проекте брать из справочников)
    private val defaultSectionId = "a1b2c3d4-0000-4000-8000-000000000001"
    private val defaultTrackId = "a1b2c3d4-0000-4000-8000-0000000000a1"

    fun onPicketChange(newValue: String) {
        _uiState.update { it.copy(picket = newValue, errorMessage = null) }
    }

    fun onCommentChange(newValue: String) {
        _uiState.update { it.copy(comment = newValue) }
    }

    fun onSeverityChange(newSeverity: Severity) {
        _uiState.update { it.copy(severity = newSeverity) }
    }

    fun saveDefect() {
        val currentState = _uiState.value
        val picketInt = currentState.picket.toIntOrNull()

        if (picketInt == null) {
            _uiState.update { it.copy(errorMessage = "Введите корректный пикет") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Формируем модель
            val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())
            val newDefect = Defect(
                id = UUID.randomUUID().toString(),
                sectionId = defaultSectionId,
                trackId = defaultTrackId,
                categoryId = 2, // Заглушка
                typeId = 14,    // Заглушка
                picket = picketInt,
                severity = currentState.severity,
                comment = currentState.comment,
                detectedAt = now,
                photoUrl = currentPhotoUri
            )

            // Вызываем UseCase (он делает валидацию пикета и сохраняет в Room)
            val result = createDefectUseCase(newDefect)

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.exceptionOrNull()?.message ?: "Ошибка")
                }
            }
        }
    }

    fun fetchLocation() {
        viewModelScope.launch {
            val location = locationProvider.getCurrentLocation()
            if (location != null) {
                // Здесь можно показать координаты (например, через Toast, но лучше в UI)
                _uiState.update { it.copy(comment = "Координаты: ${location.first}, ${location.second}") }
            } else {
                _uiState.update { it.copy(errorMessage = "Не удалось получить геолокацию (нет разрешения или сигнала)") }
            }
        }
    }

    fun toggleFlashlight(isOn: Boolean) {
        if (isOn) flashlightController.turnOn() else flashlightController.turnOff()
    }
}
