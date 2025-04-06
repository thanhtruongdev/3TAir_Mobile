package com.example.a3tair.retrofit

import com.example.a3tair.utils.Utils
import com.example.a3tair.interfaces.Apis
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    fun getInstance() : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val airQualityApi = getInstance().create(Apis::class.java)
}