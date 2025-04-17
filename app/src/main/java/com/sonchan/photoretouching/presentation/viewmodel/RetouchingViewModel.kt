package com.sonchan.photoretouching.presentation.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonchan.photoretouching.domain.model.ImageFormat
import com.sonchan.photoretouching.domain.model.RetouchingOption
import com.sonchan.photoretouching.domain.usecase.main.GetGalleryImageUseCase
import com.sonchan.photoretouching.domain.usecase.main.SaveImageToGalleryUseCase
import com.sonchan.photoretouching.domain.usecase.main.SetGalleryImageUseCase
import com.sonchan.photoretouching.util.ImageEditor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RetouchingViewModel @Inject constructor(
    private val contentResolver: ContentResolver,
    private val getGalleryImageUseCase: GetGalleryImageUseCase,
    private val setGalleryImageUseCase: SetGalleryImageUseCase,
    private val saveImageToGalleryUseCase: SaveImageToGalleryUseCase
): ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null)
    private val _openGalleryEvent = MutableSharedFlow<Unit>()
    private val _saveResult = MutableStateFlow<Boolean?>(null)
    private val _selectedFormat = MutableStateFlow<ImageFormat>(ImageFormat.JPG)
    private val _isFormatMenuExpanded = MutableStateFlow<Boolean>(false)
    private val _selectedRetouchingOption = MutableStateFlow<RetouchingOption?>(null)
    private val _retouchingValues = MutableStateFlow(
        RetouchingOption.entries.associateWith { it.defaultValue }
    )
    private val _retouchedBitmap = MutableStateFlow<Bitmap?>(null)

    val imageUri: StateFlow<Uri?> = _imageUri
    val openGalleryEvent: SharedFlow<Unit> = _openGalleryEvent
    val saveResult: SharedFlow<Boolean?> = _saveResult
    val selectedFormat: StateFlow<ImageFormat> = _selectedFormat
    val isFormatMenuExpanded: StateFlow<Boolean> = _isFormatMenuExpanded
    val selectedRetouchingOption: StateFlow<RetouchingOption?> = _selectedRetouchingOption
    val retouchingValues: StateFlow<Map<RetouchingOption, Int>> = _retouchingValues
    val retouchedBitmap: StateFlow<Bitmap?> = _retouchedBitmap

    init {
        observeGalleryImage()
    }

    private fun observeGalleryImage(){
        getGalleryImageUseCase()
            .onEach { uri -> _imageUri.value = uri }
            .launchIn(viewModelScope)
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun applyRetouching(context: Context, original: Bitmap, values: Map<RetouchingOption, Int>): Bitmap {

        var result = original.copy(Bitmap.Config.ARGB_8888, true)

        values.forEach { (option, value) ->
            result = when (option) {
                RetouchingOption.BRIGHTNESS -> {
                    ImageEditor.applyBrightness(result, value)
                }
                RetouchingOption.EXPOSURE -> {
                    ImageEditor.applyExposure(result, value)
                }
                RetouchingOption.CONTRAST -> {
                    ImageEditor.applyConstruct(result, value)
                }
                RetouchingOption.HIGHLIGHT -> {
                    ImageEditor.applyHighlight(context, result, value)
                }
                RetouchingOption.SHADOW -> {
                    ImageEditor.applyShadow(result, value)
                }
                else -> result
            }
        }

        return result
    }

    suspend fun updateGalleryImage(uri: Uri?) {
        setGalleryImageUseCase(uri)
        _retouchingValues.value = RetouchingOption.entries.associateWith { it.defaultValue }
        _selectedRetouchingOption.value = null
        _retouchedBitmap.value = null
    }

    fun requestOpenGallery() {
        viewModelScope.launch {
            _openGalleryEvent.emit(Unit)
        }
    }

    fun saveImage() {
        viewModelScope.launch {
            val bitmap = retouchedBitmap.value ?: uriToBitmap(imageUri.value ?: return@launch)
            bitmap?.let {
                val result = saveImageToGalleryUseCase(it, selectedFormat.value)
                _saveResult.value = result
            }
        }
    }

    fun updateSelectedFormat(format: ImageFormat) {
        _selectedFormat.value = format
    }

    fun onExpandFormatMenu() {
        _isFormatMenuExpanded.value = true
    }

    fun onDismissFormatMenu() {
        _isFormatMenuExpanded.value = false
    }

    fun clearSaveResult() {
        _saveResult.value = null
    }

    fun selectRetouchingOption(option: RetouchingOption) {
        _selectedRetouchingOption.value = option
    }

    fun updateRetouchingValue(context: Context, option: RetouchingOption, newValue: Int) {
        _retouchingValues.update { it.toMutableMap().apply { put(option, newValue) } }

        val bitmap = imageUri.value?.let { uri ->
            uriToBitmap(uri)
        }

        if (bitmap != null) {
            val edited = applyRetouching(context, bitmap, _retouchingValues.value)
            _retouchedBitmap.value = edited
        } else {
            Log.e("로그", "이미지 로딩 실패")
        }

        Log.d("로그", "option -> $option value -> $newValue")
    }

    fun resetRetouchingValue(context: Context, option: RetouchingOption) {
        updateRetouchingValue(context, option, option.defaultValue)
    }
}
