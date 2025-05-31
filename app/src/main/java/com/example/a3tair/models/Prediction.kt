package com.example.a3tair.models

import java.time.LocalDateTime
import java.util.Date

data class Prediction(
    val airQuality: Int,
    val createdAt: String,
    val dust: Double,
    val humidity: Double,
    val prediction_id: Int,
    val temperature: Double
)