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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun MainRoute(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val imageUri by viewModel.imageUri.collectAsState()

    MainScreen(
        modifier = modifier,
        onGalleryOpenRequest = { viewModel.requestOpenGallery() },
        onImageSelected = { uri -> viewModel.updateGalleryImage(uri) },
        imageUri = imageUri,
        openGalleryEvent = viewModel.openGalleryEvent
    )
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onGalleryOpenRequest: () -> Unit,
    onImageSelected: (Uri) -> Unit,
    imageUri: Uri?,
    openGalleryEvent: SharedFlow<Unit>
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    // 🔥 ViewModel의 이벤트를 감지해서 갤러리를 실행
    LaunchedEffect(Unit) {
        openGalleryEvent.collect {
            galleryLauncher.launch("image/*")
        }
    }

    Column(
        modifier = modifier.fillMaxSize().background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onGalleryOpenRequest() }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_icon),
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
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } ?: Box(
            Modifier
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
    val dummySharedFlow: SharedFlow<Unit> = MutableSharedFlow()

    PhotoRetouchingTheme {
        MainScreen(
            onGalleryOpenRequest = {},
            imageUri = null,
            onImageSelected = {},
            openGalleryEvent = dummySharedFlow,
        )
    }
}
