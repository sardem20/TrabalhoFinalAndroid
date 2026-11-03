package com.example.arablecoreapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.arablecoreapi.ui.theme.ArableCoreAPITheme
import com.example.arablecoreapi.userinterface.AppNavigation
import com.example.arablecoreapi.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArableCoreAPITheme {
                val vm: MainViewModel = viewModel()
                AppNavigation(vm)
            }
        }
    }
}
