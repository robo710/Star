package com.sonchan.photoretouching.presentation.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val imageUri by viewModel.imageUri.collectAsState()

    // 갤러리에서 이미지 선택하는 런처
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateGalleryImage(it) } // 선택한 이미지 URI를 ViewModel에 저장
    }

    Column(
        modifier = modifier.fillMaxSize().background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { galleryLauncher.launch("image/*") }) { // ✅ 버튼 클릭 시 갤러리 실행
                Icon(
                    painter = painterResource(id = R.drawable.add_icon),
                    contentDescription = "Select Image",
                    tint = Color.Black
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color.Gray)

        Box(
            modifier = Modifier.fillMaxSize().weight(1f),
            contentAlignment = Alignment.Center
        ) {
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Fit
                )
            } ?: Text(
                "이미지를 선택하세요",
                Modifier.clickable { galleryLauncher.launch("image/*") }
            )
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