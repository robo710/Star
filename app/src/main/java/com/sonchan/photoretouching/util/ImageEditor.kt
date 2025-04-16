package com.sonchan.photoretouching.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.Log

object ImageEditor {
    fun applyBrightness(bitmap: Bitmap, value: Int): Bitmap {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()
        val brightness = value * 2.55f

        Log.d("로그", "Brightness value: $brightness")

        val colorMatrix = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, brightness,
                0f, 1f, 0f, 0f, brightness,
                0f, 0f, 1f, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }

    fun applyExposure(bitmap: Bitmap, value: Int): Bitmap {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()
        val exposure = value * 2.55f

        Log.d("로그", "Brightness value: $exposure")

        val colorMatrix = ColorMatrix(
            floatArrayOf(
                exposure, 0f, 0f, 0f, 0f,
                0f, exposure, 0f, 0f, 0f,
                0f, 0f, exposure, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }
}