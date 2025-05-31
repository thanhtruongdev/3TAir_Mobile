package com.example.a3tair.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resumeWithException

class LocationService(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationData? = suspendCancellableCoroutine { continuation ->
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            continuation.resume(null) { }
            return@suspendCancellableCoroutine
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    var cityName: String? = null
                    try {
                        // Thử lấy địa chỉ
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        cityName = addresses?.firstOrNull()?.locality
                            ?: addresses?.firstOrNull()?.subAdminArea
                                    ?: "Không xác định"
                    } catch (e: Exception) {
                        // Nếu Geocoder thất bại, trả về "Không xác định"
                        cityName = "Không xác định"
                    }

                    continuation.resume(
                        LocationData(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            cityName = cityName
                        )
                    ) {}
                } else {
                    continuation.resume(null) { }
                }
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
}