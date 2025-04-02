package com.sonchan.photoretouching.domain.repository.main

import android.net.Uri

interface GalleryRepository {
    suspend fun getGalleryImage(): Uri?
}
