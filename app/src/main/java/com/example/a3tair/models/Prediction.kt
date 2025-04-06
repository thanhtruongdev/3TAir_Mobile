package com.example.a3tair.models

import java.util.Date

data class Prediction(
    val airQuality: Int,
    val createdAt: Date,
    val dust: Double,
    val humidity: Double,
    val prediction_id: Int,
    val temperature: Double
)