package com.example.a3tair.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.a3tair.models.AirQuality
import kotlin.collections.forEach

@Composable
fun HumidityChart(airQuality: List<AirQuality>?) {
    var humidityList = arrayListOf<Double>()
    airQuality?.let { it ->
        airQuality.forEach { it ->
            humidityList += it.humidity
        }
    }

    if (humidityList != null && humidityList.isNotEmpty()) {
        LineChart(
            data = humidityList,
            label = "Độ ẩm | Nhỏ nhất ${humidityList.minOrNull()} | Lớn nhất ${humidityList.maxOrNull()}",
            lineColor = listOf(Color(0xFF2196F3), Color(0xFF2196F3)),
            firstGradientFillColor = Color(0xFF2693EC),
            minValue = 0.0,
            maxValue = humidityList.maxOrNull()?.plus(5.0) ?: 0.0,
            dotColor = Color(0xFF0082E7)
        )
    }
}