package com.sonchan.photoretouching.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonchan.photoretouching.domain.usecase.main.GetGalleryImageUseCase
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
class GalleryViewModel @Inject constructor(
    private val context: Context,
    private val getGalleryImageUseCase: GetGalleryImageUseCase,
    private val setGalleryImageUseCase: SetGalleryImageUseCase
): ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null)
    private val _openGalleryEvent = MutableSharedFlow<Unit>() // 이벤트 트리거 용도

    val imageUri: StateFlow<Uri?> = _imageUri
    val openGalleryEvent: SharedFlow<Unit> = _openGalleryEvent

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

    fun updateGalleryImage(uri: Uri?){
        setGalleryImageUseCase(uri)
    }

    fun requestOpenGallery(){
        viewModelScope.launch {
            _openGalleryEvent.emit(Unit)
        }
    }

    fun uriToBitmap(uri: Uri): Bitmap?{
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception){
            null
        }
    }
}