package com.sonchan.photoretouching.domain.usecase.main

import android.net.Uri
import com.sonchan.photoretouching.domain.repository.main.RetouchingRepository
import javax.inject.Inject

class SetGalleryImageUseCase @Inject constructor(
    private val repository: RetouchingRepository
) {
    suspend operator fun invoke(uri: Uri?){
        repository.setGalleryImage(uri)
    }
}