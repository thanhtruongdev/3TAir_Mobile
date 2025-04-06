package com.example.a3tair.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3tair.models.AirQuality
import com.example.a3tair.models.Prediction
import com.example.a3tair.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class AirQualityViewModel : ViewModel() {
    private val airQualityApi = RetrofitInstance.airQualityApi

    var _airQuality = MutableLiveData<List<AirQuality>>()
    var airQuality : LiveData<List<AirQuality>> = _airQuality
    var _prediction = MutableLiveData<List<Prediction>>()
    var prediction : LiveData<List<Prediction>> = _prediction

    var isLoaded = false

    init {
        getAirQualityData()
        getPredictionData()
    }

    fun reloadData() {
        getAirQualityData()
        getPredictionData()
    }

    fun getAirQualityData(){
        viewModelScope.launch {
            val response = airQualityApi.getAirQualityData()
            try {
                if (response.isSuccessful) {
                    isLoaded = true
                    Log.i("DATA", response.body().toString())
                    response.body()?.let {
                        _airQuality.value = it.airQualityDtos
                    }
                } else {
                    isLoaded = false
                    println("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Error fetching data: ${e.message}")
            }
        }
    }

    fun getPredictionData(){
        viewModelScope.launch {
            val response = airQualityApi.getPredictionData()
            try {
                if (response.isSuccessful) {
                    Log.i("DATA", response.body().toString())
                    response.body()?.let {
                        _prediction.value = it.predictionDtos
                    }
                } else {
                    println("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Error fetching data: ${e.message}")
            }
        }
    }

}