package com.sonchan.photoretouching.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.sonchan.photoretouching.R
import com.sonchan.photoretouching.presentation.viewmodel.GalleryViewModel
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val imageUri by viewModel.imageUri.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Spacer(modifier = modifier.weight(1f))
            IconButton(onClick = { /* TODO: 갤러리 열기 */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_icon),
                    contentDescription = "",
                    modifier = modifier,
                    tint = Color.Black
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color.Gray)

        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@DevicePreviews
@Composable
fun MainScreenPreview(){
    PhotoRetouchingTheme {
        MainScreen()
    }
}