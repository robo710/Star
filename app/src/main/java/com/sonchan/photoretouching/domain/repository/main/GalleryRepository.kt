package com.sonchan.photoretouching.domain.repository.main

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getGalleryImage(): Flow<Uri?>
    fun setGalleryImage(uri: Uri?)
}
