package com.sonchan.photoretouching.domain.usecase.main

import android.net.Uri
import com.sonchan.photoretouching.domain.repository.main.GalleryRepository
import javax.inject.Inject

class SetGalleryImageUseCase @Inject constructor(
    private val repository: GalleryRepository
) {
    suspend operator fun invoke(uri: Uri?){
        repository.setGalleryImage(uri)
    }
}