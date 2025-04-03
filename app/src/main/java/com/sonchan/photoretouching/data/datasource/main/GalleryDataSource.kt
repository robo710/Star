package com.sonchan.photoretouching.data.datasource.main

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface GalleryDataSource {
    val imageUri: Flow<Uri?>
    fun setImageUri(uri: Uri?)
}