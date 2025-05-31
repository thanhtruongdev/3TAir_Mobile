package com.example.a3tair.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.a3tair.models.AirQuality
import kotlin.collections.forEach

@Composable
fun DustChart(airQuality: List<AirQuality>?) {
    val dustList = arrayListOf<Double>()
    airQuality?.let { it ->
        airQuality.reversed().forEach { it ->
            dustList += it.dust
        }
    }

    if (dustList != null && dustList.isNotEmpty()) {
        LineChart(
            data = dustList,
            label = "PM2.5 | Nhỏ nhất ${dustList.minOrNull()} | Lớn nhất ${dustList.maxOrNull()}",
            lineColor = listOf(Color(0xFFFFC107), Color(0xFFFFC107)),
            firstGradientFillColor = Color(0xFFFFD557),
            minValue = 0.0,
            maxValue = dustList.maxOrNull()?.plus(5) ?: 0.0,
            dotColor = Color(0xFFE7AD0E)
        )
    }
}