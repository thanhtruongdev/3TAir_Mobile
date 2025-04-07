package com.example.a3tair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.a3tair.ui.MainView
import com.example.a3tair.ui.TopAppBar
import com.example.a3tair.ui.theme._3TAirTheme
import com.example.a3tair.viewModel.AirQualityViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this)[AirQualityViewModel::class.java]
        setContent {
            _3TAirTheme {
                Scaffold(
                    topBar = { TopAppBar() }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        MainView(viewModel)
                    }
                }
            }
        }
    }
}



