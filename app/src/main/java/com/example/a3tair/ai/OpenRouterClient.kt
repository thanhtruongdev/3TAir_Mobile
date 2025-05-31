package com.example.chillzone.ai

import com.example.a3tair.interfaces.OpenRouterApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenRouterClient {
    private const val BASE_URL = "https://openrouter.ai/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    val service: OpenRouterApi by lazy {
        retrofit.create(OpenRouterApi::class.java)
    }
}