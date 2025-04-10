package com.sonchan.photoretouching.presentation.screen

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import coil.compose.rememberAsyncImagePainter
import com.sonchan.photoretouching.R
import com.sonchan.photoretouching.domain.model.ImageFormat
import com.sonchan.photoretouching.domain.model.RetouchingOption
import com.sonchan.photoretouching.presentation.component.DevicePreviews
import com.sonchan.photoretouching.presentation.component.ImageFormatDropDown
import com.sonchan.photoretouching.presentation.component.RetouchingOptions
import com.sonchan.photoretouching.presentation.component.RetouchingToast
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
    val saveResult by viewModel.saveResult.collectAsState(initial = null)
    val selectedFormat by viewModel.selectedFormat.collectAsState()
    val isFormatMenuExpanded by viewModel.isFormatMenuExpanded.collectAsState()
    val selectedRetouchingOption by viewModel.selectedRetouchingOption.collectAsState()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current

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

    LaunchedEffect(saveResult) {
        saveResult?.let { result ->
            val message = if (result) "이미지 저장 성공!" else "저장 실패 😥"
            RetouchingToast(context).showToast(
                message = message,
                icon = if (result) R.drawable.success_icon else R.drawable.fail_icon,
                duration = Toast.LENGTH_SHORT,
                lifecycleOwner = lifecycleOwner,
                viewModelStoreOwner = viewModelStoreOwner!!,
                savedStateRegistryOwner = savedStateRegistryOwner
            )
            viewModel.clearSaveResult()
        }
    }

    RetouchingScreen(
        modifier = modifier,
        onGalleryOpenRequest = { viewModel.requestOpenGallery() },
        onSaveImageRequest = { viewModel.saveImage() },
        imageUri = imageUri,
        selectedFormat = selectedFormat,
        onSelectFormat = { viewModel.updateSelectedFormat(it) },
        isFormatMenuExpanded = isFormatMenuExpanded,
        onExpandFormatMenu = { viewModel.onExpandFormatMenu() },
        onDismissFormatMenu = { viewModel.onDismissFormatMenu() },
        selectedOption = selectedRetouchingOption,
        selectRetouchingOption = { viewModel.selectRetouchingOption(it) }
    )
}

@Composable
fun RetouchingScreen(
    modifier: Modifier = Modifier,
    onGalleryOpenRequest: () -> Unit,
    onSaveImageRequest: (ImageFormat) -> Unit,
    imageUri: Uri?,
    selectedFormat: ImageFormat,
    onSelectFormat: (ImageFormat) -> Unit,
    isFormatMenuExpanded: Boolean,
    onExpandFormatMenu: () -> Unit,
    onDismissFormatMenu: () -> Unit,
    selectedOption: RetouchingOption?,
    selectRetouchingOption: (RetouchingOption) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Spacer(modifier = modifier.weight(1f))
            ImageFormatDropDown(
                selectedFormat = selectedFormat,
                isFormatMenuExpanded = isFormatMenuExpanded,
                onExpandFormatMenu = onExpandFormatMenu,
                onDismissFormatMenu = onDismissFormatMenu,
                onSelectFormat = onSelectFormat
            )
            IconButton(onClick = { onSaveImageRequest(selectedFormat) }) {
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
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        } ?: Box(
            modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable { onGalleryOpenRequest() },
            contentAlignment = Alignment.Center
        ) {
            Text("이미지를 선택하세요")
        }
        RetouchingOptions(
            options = RetouchingOption.values().toList(),
            onOptionSelected = selectRetouchingOption,
            selectedOption = selectedOption
        )
    }
}


@DevicePreviews
@Composable
fun MainScreenPreview() {
    PhotoRetouchingTheme {
        RetouchingScreen(
            onGalleryOpenRequest = {},
            onSaveImageRequest = {},
            imageUri = null,
            selectedFormat = ImageFormat.JPG,
            onSelectFormat = {},
            isFormatMenuExpanded = false,
            onExpandFormatMenu = {},
            onDismissFormatMenu = {},
            selectedOption = RetouchingOption.BRIGHTNESS,
            selectRetouchingOption = {}
        )
    }
}
