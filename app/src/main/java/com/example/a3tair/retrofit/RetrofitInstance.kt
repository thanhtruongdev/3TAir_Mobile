package com.example.a3tair.retrofit

import com.example.a3tair.utils.Utils
import com.example.a3tair.interfaces.Apis
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun getInstance() : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(Utils.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val airQualityApi = getInstance().create(Apis::class.java)
}