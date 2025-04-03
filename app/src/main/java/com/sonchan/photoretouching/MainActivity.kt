package com.sonchan.photoretouching

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sonchan.photoretouching.presentation.screen.MainScreen
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoRetouchingTheme {
                MainScreen()
            }
        }
    }
}