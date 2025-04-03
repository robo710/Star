package com.sonchan.photoretouching.domain.usecase.main

import android.net.Uri
import com.sonchan.photoretouching.domain.repository.main.GalleryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGalleryImageUseCase @Inject constructor(
    private val repository: GalleryRepository
) {
    operator fun invoke(): Flow<Uri?> {
        return repository.getGalleryImage()
    }
}