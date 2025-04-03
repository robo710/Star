package com.sonchan.photoretouching.data.repository.main

import android.net.Uri
import com.sonchan.photoretouching.data.datasource.main.GalleryDataSource
import com.sonchan.photoretouching.domain.repository.main.GalleryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryDataSource: GalleryDataSource
): GalleryRepository {
    override fun getGalleryImage(): Flow<Uri?> {
        return galleryDataSource.imageUri
    }

    override  fun setGalleryImage(uri: Uri?) {
        return galleryDataSource.setImageUri(uri)
    }
}