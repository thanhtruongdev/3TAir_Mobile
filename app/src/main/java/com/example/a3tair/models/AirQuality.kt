package com.example.a3tair.models

import java.util.Date

data class AirQuality(
    val airQuality: Int,
    val createdAt: Date,
    val dust: Double,
    val humidity: Double,
    val id: Int,
    val temperature: Double
)