package com.example.a3tair.interfaces

import retrofit2.Response
import retrofit2.http.GET

interface Apis {
    @GET("api/get-air-quality")
    suspend fun getAirQualityData() : Response<com.example.a3tair.models.Response>

    @GET("api/get-prediction")
    suspend fun getPredictionData() : Response<com.example.a3tair.models.Response>
}