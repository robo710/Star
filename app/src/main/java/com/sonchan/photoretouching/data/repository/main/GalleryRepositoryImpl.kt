package com.sonchan.photoretouching.data.repository.main

import android.app.Activity
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import com.sonchan.photoretouching.data.datasource.main.GalleryDataSource
import com.sonchan.photoretouching.domain.repository.main.GalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryDataSource: GalleryDataSource
): GalleryRepository {
    override suspend fun getGalleryImage(): Flow<Uri?> {
        return galleryDataSource.imageUri
    }

    override suspend fun setGalleryImage(uri: Uri?) {
        return galleryDataSource.setImageUri(uri)
    }
}