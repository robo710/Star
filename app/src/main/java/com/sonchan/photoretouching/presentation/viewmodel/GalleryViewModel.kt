package com.sonchan.photoretouching.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonchan.photoretouching.domain.usecase.main.GetGalleryImageUseCase
import com.sonchan.photoretouching.domain.usecase.main.SetGalleryImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getGalleryImageUseCase: GetGalleryImageUseCase,
    private val setGalleryImageUseCase: SetGalleryImageUseCase
): ViewModel() {

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

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
}