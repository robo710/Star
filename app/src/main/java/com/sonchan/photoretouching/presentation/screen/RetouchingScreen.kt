package com.sonchan.photoretouching.presentation.screen

import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
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
import com.sonchan.photoretouching.presentation.component.DarkThemeDevicePreview
import com.sonchan.photoretouching.presentation.component.DevicePreviews
import com.sonchan.photoretouching.presentation.component.ImageFormatDropDown
import com.sonchan.photoretouching.presentation.component.RetouchingOptions
import com.sonchan.photoretouching.presentation.component.RetouchingSlider
import com.sonchan.photoretouching.presentation.component.RetouchingToast
import com.sonchan.photoretouching.presentation.component.ThemeToggleButton
import com.sonchan.photoretouching.presentation.viewmodel.RetouchingViewModel
import com.sonchan.photoretouching.presentation.viewmodel.ThemeViewModel
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme
import kotlinx.coroutines.launch

@Composable
fun RetouchingRoute(
    modifier: Modifier = Modifier,
    viewModel: RetouchingViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val imageUri by viewModel.imageUri.collectAsState()
    val saveResult by viewModel.saveResult.collectAsState(initial = null)
    val selectedFormat by viewModel.selectedFormat.collectAsState()
    val isFormatMenuExpanded by viewModel.isFormatMenuExpanded.collectAsState()
    val selectedRetouchingOption by viewModel.selectedRetouchingOption.collectAsState()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val retouchingValues by viewModel.retouchingValues.collectAsState()
    val retouchedBitmap by viewModel.retouchedBitmap.collectAsState()

    val sliderStates = remember { mutableMapOf<RetouchingOption, LazyListState>() }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                viewModel.updateGalleryImage(it)
            }
        }
    }

    RetouchingOption.entries.forEach { option ->
        if (!sliderStates.contains(option)) {
            sliderStates[option] = rememberLazyListState()
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
                isDarkTheme = isDarkTheme,
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
        selectRetouchingOption = { viewModel.selectRetouchingOption(it) },
        isDarkTheme = isDarkTheme,
        onToggleTheme = { themeViewModel.toggleTheme() },
        retouchingValues = retouchingValues, // 상태 값 전달
        sliderStates = sliderStates,
        updateRetouchingValue = { option, newValue ->
            viewModel.updateRetouchingValue(option, newValue)
        },
        resetRetouchingValue = { option ->
            viewModel.resetRetouchingValue(option)
        },
        bitmap = retouchedBitmap,
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
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    retouchingValues: Map<RetouchingOption, Int>,
    sliderStates: Map<RetouchingOption, LazyListState>,
    updateRetouchingValue: (RetouchingOption, Int) -> Unit,
    resetRetouchingValue: (RetouchingOption) -> Unit,
    bitmap: Bitmap?,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Spacer(modifier = modifier.weight(1f))
            ThemeToggleButton(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
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
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { onGalleryOpenRequest() }) {
                Icon(
                    painter = painterResource(R.drawable.add_icon),
                    contentDescription = "Select Image",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)

        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Edited Image",
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        } ?: imageUri?.let {
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
            Text(
                text = "이미지를 선택하세요",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        selectedOption?.let { option ->
            val sliderState = sliderStates[option] ?: rememberLazyListState()

            RetouchingSlider(
                value = retouchingValues[option] ?: option.defaultValue,
                valueRange = option.range.first..option.range.last,
                listState = sliderState,
                onValueChanged = { newValue ->
                    updateRetouchingValue(option, newValue)
                },
                tickInterval = 10,
                onResetValue = { resetRetouchingValue(option) }
            )
        }
        Box(
            modifier = modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            RetouchingOptions(
                options = RetouchingOption.entries,
                onOptionSelected = selectRetouchingOption,
                selectedOption = selectedOption,
                optionValues = retouchingValues
            )
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
            imageUri = null,
            selectedFormat = ImageFormat.JPG,
            onSelectFormat = {},
            isFormatMenuExpanded = false,
            onExpandFormatMenu = {},
            onDismissFormatMenu = {},
            selectedOption = RetouchingOption.BRIGHTNESS,
            selectRetouchingOption = {},
            isDarkTheme = false,
            onToggleTheme = {},
            retouchingValues = mapOf(RetouchingOption.BRIGHTNESS to 50),
            sliderStates = mapOf(RetouchingOption.BRIGHTNESS to rememberLazyListState()),
            updateRetouchingValue = { _, _ -> },
            resetRetouchingValue = {},
            bitmap = null
        )
    }
}

@DarkThemeDevicePreview
@Composable
fun MainScreenDarkThemePreview() {
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
            selectRetouchingOption = {},
            isDarkTheme = true,
            onToggleTheme = {},
            retouchingValues = mapOf(RetouchingOption.BRIGHTNESS to 50),
            sliderStates = mapOf(RetouchingOption.BRIGHTNESS to rememberLazyListState()),
            updateRetouchingValue = { _, _ -> },
            resetRetouchingValue = {},
            bitmap = null
        )
    }
}
