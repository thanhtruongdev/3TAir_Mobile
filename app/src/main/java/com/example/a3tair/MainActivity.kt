package com.example.a3tair

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.a3tair.ui.MainView
import com.example.a3tair.ui.TopAppBar
import com.example.a3tair.ui.theme._3TAirTheme
import com.example.a3tair.viewModel.AiViewModel
import com.example.a3tair.viewModel.AirQualityViewModel
import com.example.a3tair.widget.scheduleWidgetUpdate

class MainActivity : ComponentActivity() {
    private val viewModel: AirQualityViewModel by viewModels()
    private val aiViewModel: AiViewModel by viewModels()
    val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.fetchLocation(this)
        } else {
            Toast.makeText(this, "Vui lòng cấp quyền truy cập vị trí", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        scheduleWidgetUpdate(this)
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.fetchLocation(this)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
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
                        MainView(viewModel,aiViewModel)
                    }
                }
            }
        }
    }
}





