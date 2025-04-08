package com.sonchan.photoretouching.data.datasource.main

import android.graphics.Bitmap
import android.net.Uri
import com.sonchan.photoretouching.domain.model.ImageFormat
import kotlinx.coroutines.flow.Flow

interface RetouchingDataSource {
    val imageUri: Flow<Uri?>
    suspend fun setImageUri(uri: Uri?)
    suspend fun saveImage(bitmap: Bitmap, format: ImageFormat): Boolean
}