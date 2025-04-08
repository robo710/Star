package com.sonchan.photoretouching.data.datasource.main

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.sonchan.photoretouching.domain.model.ImageFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class RetouchingDataSourceImpl @Inject constructor(
    private val contentResolver: ContentResolver
): RetouchingDataSource{
    private val _imageUri = MutableStateFlow<Uri?>(null)
    override val imageUri: Flow<Uri?> = _imageUri

    override suspend fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    override suspend fun saveImage(bitmap: Bitmap, format: ImageFormat): Boolean {
        return try {
            val filename = "IMG_${System.currentTimeMillis()}.${format.name.lowercase()}"
            val mimeType = when(format){
                ImageFormat.JPG -> "image/jpeg"
                ImageFormat.PNG -> "image/png"
                ImageFormat.WEBP -> "image/webp"
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename) // 파일 이름
                put(MediaStore.Images.Media.MIME_TYPE, mimeType) // 미디어 타입
                put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/PR") // 이미지 저장 경로
                put(MediaStore.Images.Media.IS_PENDING, 1) // 이미지 저장중이라는 상태
            }

            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: return false // 이미지 저장 공간 요청 후 그 위치를 나타내는 URI 받아오는 코드

            contentResolver.openOutputStream(uri)?.use { outputStream ->
                val compressFormat = when (format) {
                    ImageFormat.JPG -> Bitmap.CompressFormat.JPEG
                    ImageFormat.PNG -> Bitmap.CompressFormat.PNG
                    ImageFormat.WEBP -> Bitmap.CompressFormat.WEBP
                }
                bitmap.compress(compressFormat, 100, outputStream)
            }

            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)

            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }
}