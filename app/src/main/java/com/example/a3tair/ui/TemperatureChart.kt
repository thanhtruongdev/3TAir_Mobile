package com.example.a3tair.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.a3tair.models.AirQuality
import kotlin.collections.forEach

@Composable
fun TemperatureChart(airQuality : List<AirQuality>?) {
    var tempList = arrayListOf<Double>()
    airQuality?.let { it ->
        airQuality.reversed().forEach { it ->
            tempList += it.temperature
        }
    }

    if (tempList != null && tempList.isNotEmpty()) {
        Log.i("MINVALUE", tempList.minOrNull().toString())
        Log.i("VALUE", tempList.toString())
        LineChart(
            data = tempList,
            label = "Nhiệt độ | Nhỏ nhất ${tempList.minOrNull()} | Lớn nhất ${tempList.maxOrNull()}",
            lineColor = listOf(Color(0xFFF44336), Color(0xFFF44336)),
            firstGradientFillColor = Color(0xFFFF4F4A),
            minValue = tempList.minOrNull() ?: 0.0,
            maxValue = tempList.maxOrNull()?.plus(5.0) ?: 0.0,
            dotColor = Color(0xFFE50900)
        )
    }
}