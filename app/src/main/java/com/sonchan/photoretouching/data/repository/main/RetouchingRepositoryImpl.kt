package com.sonchan.photoretouching.data.repository.main

import android.graphics.Bitmap
import android.net.Uri
import com.sonchan.photoretouching.data.datasource.main.RetouchingDataSource
import com.sonchan.photoretouching.domain.model.ImageFormat
import com.sonchan.photoretouching.domain.repository.main.RetouchingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetouchingRepositoryImpl @Inject constructor(
    private val galleryDataSource: RetouchingDataSource
): RetouchingRepository {
    override fun getGalleryImage(): Flow<Uri?> {
        return galleryDataSource.imageUri
    }

    override suspend fun setGalleryImage(uri: Uri?) {
        return galleryDataSource.setImageUri(uri)
    }

    override suspend fun saveImage(bitmap: Bitmap, format: ImageFormat): Boolean {
        return galleryDataSource.saveImage(bitmap, format)
    }
}