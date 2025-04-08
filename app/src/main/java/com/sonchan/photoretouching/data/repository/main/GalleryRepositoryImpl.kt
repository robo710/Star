package com.sonchan.photoretouching.data.repository.main

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.sonchan.photoretouching.data.datasource.main.GalleryDataSource
import com.sonchan.photoretouching.domain.model.ImageFormat
import com.sonchan.photoretouching.domain.repository.main.GalleryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryDataSource: GalleryDataSource
): GalleryRepository {
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