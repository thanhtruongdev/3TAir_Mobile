package com.example.a3tair.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

data class AirQuality(
    val airQuality: Int,
    var createdAt: String,
    val dust: Double,
    val humidity: Double,
    val id: Int,
    val temperature: Double
)