package com.example.metroosmotr.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.example.metroosmotr.domain.repository.LocationProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationProvider {

    override suspend fun getCurrentLocation(): Pair<Double, Double>? = withContext(Dispatchers.IO) {
        // Проверка разрешения
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return@withContext null
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Пытаемся получить последнюю известную локацию (без ожидания GPS)
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        location?.let { Pair(it.latitude, it.longitude) }
    }
}