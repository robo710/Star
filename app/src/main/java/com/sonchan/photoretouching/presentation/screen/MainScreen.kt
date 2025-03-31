package com.sonchan.photoretouching.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .background(color = Color(0xFFFFFFFF))
            .fillMaxSize()
    ){
    }
}

@DevicePreviews
@Composable
fun MainScreenPreview(){
    MainScreen()
}