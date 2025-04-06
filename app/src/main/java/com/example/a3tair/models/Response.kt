package com.example.a3tair.models

data class Response (
    val status : Int,
    val message : String,
    val airQualityDtos : List<AirQuality>,
    val predictionDtos : List<Prediction>
)