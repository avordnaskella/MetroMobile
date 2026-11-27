package com.example.metroosmotr.presentation.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private val _uiState = MutableStateFlow(CreateUiState())
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()

    // --- СОСТОЯНИЯ ДЛЯ ВЫПАДАЮЩИХ СПИСКОВ (публичные для UI) ---
    var selectedSection by mutableStateOf("Перегон А — Б")
    var selectedTrack by mutableStateOf("Главный путь 1")
    var selectedCategory by mutableStateOf("Крепления (болты, клеммы)")
    var selectedType by mutableStateOf("Расшатан болт")

    // --- СПИСКИ ОПЦИЙ ---
    val sections = listOf("Перегон А — Б", "Перегон Б — В")
    val tracks = listOf("Главный путь 1", "Главный путь 2")
    val categories = listOf("Рельсовое полотно", "Крепления (болты, клеммы)", "Земляное полотно")
    val types = listOf("Расшатан болт", "Трещина рельса", "Просадка пути")

    init {
        // Устанавливаем текущую дату при создании экрана
        val currentDate = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault()).format(Date())
        _uiState.update { it.copy(date = currentDate) }
    }


    fun onPicketChange(newValue: String) {
        // Разрешаем только цифры
        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
            _uiState.update { it.copy(picket = newValue, errorMessage = null) }
        }
    }

    fun onMetersChange(newValue: String) {
        // Разрешаем только цифры
        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
            _uiState.update { it.copy(meters = newValue) }
        }
    }
    fun onCommentChange(newValue: String) {
        _uiState.update { it.copy(comment = newValue) }
    }

    fun onSeverityChange(newSeverity: Severity) {
        _uiState.update { it.copy(severity = newSeverity) }
    }

    fun onPhotoCaptured(uri: String) {
        currentPhotoUri = uri
    }

    // --- СОХРАНЕНИЕ ---
    fun saveDefect() {
        val currentState = _uiState.value
        val picketInt = currentState.picket.toIntOrNull()

        if (picketInt == null) {
            _uiState.update { it.copy(errorMessage = "Введите корректный пикет") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())

            // В реальном проекте тут нужно мапить выбранные строки в ID из БД
            val newDefect = Defect(
                id = UUID.randomUUID().toString(),
                sectionId = "a1b2c3d4-0000-4000-8000-000000000001",
                trackId = "a1b2c3d4-0000-4000-8000-0000000000a1",
                categoryId = 2,
                typeId = 14,
                picket = picketInt,
                severity = currentState.severity,
                comment = currentState.comment,
                detectedAt = now,
                photoUrl = currentPhotoUri
            )

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

    // --- GPS ---
    fun fetchLocation() {
        viewModelScope.launch {
            val location = locationProvider.getCurrentLocation()
            if (location != null) {
                // Пишем координаты в ОТДЕЛЬНОЕ поле, а не в комментарий
                _uiState.update { it.copy(gpsCoordinates = "GPS: ${location.first}, ${location.second}") }
            } else {
                _uiState.update { it.copy(errorMessage = "Геолокация недоступна") }
            }
        }
    }

    // --- ФОНАРИК ---
    fun toggleFlashlight(isOn: Boolean) {
        if (isOn) flashlightController.turnOn() else flashlightController.turnOff()
    }
}
