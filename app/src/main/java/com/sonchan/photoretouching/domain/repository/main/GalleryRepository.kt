package com.sonchan.photoretouching.domain.repository.main

import android.graphics.Bitmap
import android.net.Uri
import com.sonchan.photoretouching.domain.model.ImageFormat
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getGalleryImage(): Flow<Uri?>
    suspend fun setGalleryImage(uri: Uri?)
    suspend fun saveImage(bitmap: Bitmap, format: ImageFormat): Boolean
}
