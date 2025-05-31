package com.example.a3tair.widget

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.a3tair.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class WidgetWorker (appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params){
    companion object {
        private const val TAG = "WidgetWorker"
    }

    override suspend fun doWork(): Result {
        val sharedPrefs = applicationContext.getSharedPreferences("air_quality", Context.MODE_PRIVATE)
        return try {
            Log.d(TAG, "Starting work")
            val response = withContext(Dispatchers.IO) {
                RetrofitInstance.airQualityApi.getAirQualityData()
            }
            Log.d(TAG, "API response: isSuccessful = ${response.isSuccessful}, code = ${response.code()}, body = ${response.body()}")

            if (!response.isSuccessful) {
                Log.e(TAG, "API failed with code: ${response.code()}, message: ${response.message()}")
                throw HttpException(response)
            }

            val data = response.body()?.airQualityDtos?.firstOrNull()
            if (data == null) {
                Log.e(TAG, "No data available: body = ${response.body()}")
                throw IllegalStateException("No air quality data received")
            }

            Log.d(TAG, "Saving data: AQI = ${data.airQuality}, PM2.5 = ${data.dust}")
            with(sharedPrefs.edit()) {
                putInt("id", data.id)
                putString("created_at", data.createdAt.toString())
                putInt("aqi", data.airQuality)
                putFloat("pm25", data.dust.toFloat())
                putFloat("temperature", data.temperature.toFloat())
                putFloat("humidity", data.humidity.toFloat())
                putString("error", null)
                apply()
            }
            Log.i(TAG, "Data saved: $data")
            Log.d(TAG, "After save: aqi = ${sharedPrefs.getInt("aqi", -1)}, pm25 = ${sharedPrefs.getFloat("pm25", -1f)}")

            Widget().forceUpdate(applicationContext)
            Log.d(TAG, "Widget update triggered")

            Result.success(
                workDataOf(
                    "id" to data.id,
                    "created_at" to data.createdAt.toString(),
                    "aqi" to data.airQuality,
                    "pm25" to data.dust,
                    "temperature" to data.temperature,
                    "humidity" to data.humidity
                )
            )
        } catch (e: IOException) {
            Log.e(TAG, "Network error: ${e.message}")
            with(sharedPrefs.edit()) {
                putString("error", "Không có kết nối mạng")
                apply()
            }
            Log.d(TAG, "Widget update triggered with error")
            if (sharedPrefs.contains("aqi")) {
                Log.d(TAG, "Using cached data")
                return Result.success()
            }
            Widget().forceUpdate(applicationContext)
            return Result.retry()
        } catch (e: HttpException) {
            Widget().forceUpdate(applicationContext)
            Log.e(TAG, "HTTP error: ${e.code()} - ${e.message()}")
            with(sharedPrefs.edit()) {
                putString("error", "Lỗi API: ${e.code()}")
                apply()
            }
            Log.d(TAG, "Widget update triggered with error")
            return Result.failure(workDataOf("error" to "HTTP ${e.code()}"))
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.message}", e)
            with(sharedPrefs.edit()) {
                putString("error", e.message ?: "Lỗi không xác định")
                apply()
            }
            Widget().forceUpdate(applicationContext)
            Log.d(TAG, "Widget update triggered with error")
            return Result.failure(workDataOf("error" to e.message))
        }
    }
}