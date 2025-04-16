package com.sonchan.photoretouching.presentation.viewmodel

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
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
import java.sql.Statement
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

    suspend fun updateGalleryImage(uri: Uri?) {
        setGalleryImageUseCase(uri)
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

    fun updateRetouchingValue(option: RetouchingOption, newValue: Int) {
        // 보정 값을 업데이트
        _retouchingValues.update { it.toMutableMap().apply { put(option, newValue) } }
        Log.d("로그", "retouchedBitmap -> ${retouchedBitmap.value}")

        val bitmap = imageUri.value?.let { uri ->
            uriToBitmap(uri)
        }

        if (bitmap != null) {
            val edited = applyRetouching(bitmap, _retouchingValues.value)
            _retouchedBitmap.value = edited // 보정된 비트맵 업데이트
        } else {
            Log.e("로그", "이미지 로딩 실패")
        }

        Log.d("로그", "option -> ${option} value -> ${newValue}")
    }

    fun resetRetouchingValue(option: RetouchingOption) {
        updateRetouchingValue(option, option.defaultValue)
    }

    fun applyRetouching(original: Bitmap, values: Map<RetouchingOption, Int>): Bitmap {
        Log.d("로그", "applyRetouching 호출됨")

        var result = original.copy(Bitmap.Config.ARGB_8888, true) // 원본 이미지를 복사하여 결과 비트맵으로 설정

        values.forEach { (option, value) ->
            Log.d("로그", "보정 옵션: ${option}, 값: ${value}")
            result = when (option) {
                RetouchingOption.BRIGHTNESS -> {
                    Log.d("로그", "밝기 보정 적용 중")
                    ImageEditor.applyBrightness(result, value)
                }
                else -> result
            }
        }

        return result
    }
}
