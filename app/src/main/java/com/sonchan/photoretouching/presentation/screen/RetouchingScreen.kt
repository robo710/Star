package com.sonchan.photoretouching.presentation.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.sonchan.photoretouching.R
import com.sonchan.photoretouching.presentation.component.DevicePreviews
import com.sonchan.photoretouching.presentation.viewmodel.RetouchingViewModel
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme
import kotlinx.coroutines.launch

@Composable
fun RetouchingRoute(
    modifier: Modifier = Modifier,
    viewModel: RetouchingViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val imageUri by viewModel.imageUri.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                viewModel.updateGalleryImage(it) }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.openGalleryEvent.collect {
            galleryLauncher.launch("image/*")
        }
    }

    RetouchingScreen(
        modifier = modifier,
        onGalleryOpenRequest = { viewModel.requestOpenGallery() },
        onSaveImageRequest = { },
        imageUri = imageUri
    )
}

@Composable
fun RetouchingScreen(
    modifier: Modifier = Modifier,
    onGalleryOpenRequest: () -> Unit,
    onSaveImageRequest: () -> Unit,
    imageUri: Uri?,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Spacer(modifier = modifier.weight(1f))
            IconButton(onClick = { onSaveImageRequest() }) {
                Icon(
                    painter = painterResource(R.drawable.download_icon),
                    contentDescription = "Download Icon",
                    tint = Color.Black
                )
            }
            IconButton(onClick = { onGalleryOpenRequest() }) {
                Icon(
                    painter = painterResource(R.drawable.add_icon),
                    contentDescription = "Select Image",
                    tint = Color.Black
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color.Gray)

        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Selected Image",
                modifier = modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } ?: Box(
            modifier
                .fillMaxSize()
                .clickable { onGalleryOpenRequest() },
            contentAlignment = Alignment.Center
        ) {
            Text("이미지를 선택하세요")
        }
    }
}


@DevicePreviews
@Composable
fun MainScreenPreview() {
    PhotoRetouchingTheme {
        RetouchingScreen(
            onGalleryOpenRequest = {},
            onSaveImageRequest = {},
            imageUri = null
        )
    }
}
