package com.sonchan.photoretouching.data.datasource.main

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GalleryDataSourceImpl @Inject constructor(): GalleryDataSource{
    private val _imageUri = MutableStateFlow<Uri?>(null)
    override val imageUri: Flow<Uri?> = _imageUri

    override fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }
}