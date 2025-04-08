package com.sonchan.photoretouching.domain.usecase.main

import android.graphics.Bitmap
import com.sonchan.photoretouching.domain.model.ImageFormat
import com.sonchan.photoretouching.domain.repository.main.RetouchingRepository
import javax.inject.Inject

class SaveImageToGalleryUseCase @Inject constructor(
    private val repository: RetouchingRepository
) {
    suspend operator fun invoke(bitmap: Bitmap, format: ImageFormat): Boolean{
        return repository.saveImage(bitmap, format)
    }
}