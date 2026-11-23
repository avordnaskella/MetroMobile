package com.example.metroosmotr.data

import android.content.Context
import android.hardware.camera2.CameraManager
import com.example.metroosmotr.domain.repository.FlashlightController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FlashlightControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FlashlightController {

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
        val characteristics = cameraManager.getCameraCharacteristics(id)
        characteristics.get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
    }

    override fun turnOn() {
        cameraId?.let {
            try {
                cameraManager.setTorchMode(it, true)
            } catch (e: Exception) {
                // Игнорируем ошибки, если фонарик занят камерой
            }
        }
    }

    override fun turnOff() {
        cameraId?.let {
            try {
                cameraManager.setTorchMode(it, false)
            } catch (e: Exception) {
                // Игнорируем
            }
        }
    }
}