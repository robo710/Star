package com.sonchan.photoretouching.presentation.viewmodel

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonchan.photoretouching.domain.model.ImageFormat
import com.sonchan.photoretouching.domain.usecase.main.GetGalleryImageUseCase
import com.sonchan.photoretouching.domain.usecase.main.SaveImageToGalleryUseCase
import com.sonchan.photoretouching.domain.usecase.main.SetGalleryImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val _openGalleryEvent = MutableSharedFlow<Unit>() // 이벤트 트리거 용도
    private val _saveResult = MutableStateFlow<Boolean?>(null)
    private val _selectedFormat = MutableStateFlow<ImageFormat>(ImageFormat.JPG)
    private val _isFormatMenuExpanded = MutableStateFlow<Boolean>(false)

    val imageUri: StateFlow<Uri?> = _imageUri
    val openGalleryEvent: SharedFlow<Unit> = _openGalleryEvent
    val saveResult: SharedFlow<Boolean?> = _saveResult
    val selectedFormat: StateFlow<ImageFormat> = _selectedFormat
    val isFormatMenuExpanded: StateFlow<Boolean> = _isFormatMenuExpanded

    init {
        observeGalleryImage()
    }

    private fun observeGalleryImage(){
        getGalleryImageUseCase()
            .onEach { uri ->
                _imageUri.value = uri
            }
            .launchIn(viewModelScope)
    }

    private fun uriToBitmap(uri: Uri): Bitmap?{
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
        } catch (e: Exception){
            null
        }
    }

    suspend fun updateGalleryImage(uri: Uri?){
        setGalleryImageUseCase(uri)
    }

    fun requestOpenGallery(){
        viewModelScope.launch {
            _openGalleryEvent.emit(Unit)
        }
    }

    fun saveImage() {
        imageUri.value?.let { uri ->
            val bitmap = uriToBitmap(uri)
            bitmap?.let {
                viewModelScope.launch {
                    val result = saveImageToGalleryUseCase(it, selectedFormat.value)
                    _saveResult.value = result
                }
            }
        }
    }

    fun updateSelectedFormat(format: ImageFormat){
        _selectedFormat.value = format
    }

    fun onExpandFormatMenu() {
        _isFormatMenuExpanded.value = true
    }

    fun onDismissFormatMenu() {
        _isFormatMenuExpanded.value = false
    }

    fun clearSaveResult(){
        _saveResult.value = null
    }
}