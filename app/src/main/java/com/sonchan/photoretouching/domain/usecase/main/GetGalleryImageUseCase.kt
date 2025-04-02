package com.sonchan.photoretouching.domain.usecase.main

import android.net.Uri
import com.sonchan.photoretouching.domain.repository.main.GalleryRepository
import javax.inject.Inject

class GetGalleryImageUseCase @Inject constructor(
    private val repository: GalleryRepository
) {
    suspend operator fun invoke(): Uri? {
        return repository.getGalleryImage()
    }
}