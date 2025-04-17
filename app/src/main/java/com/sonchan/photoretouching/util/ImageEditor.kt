package com.sonchan.photoretouching.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.Log
import kotlin.math.pow
import androidx.core.graphics.createBitmap

object ImageEditor {
    fun applyBrightness(bitmap: Bitmap, value: Int): Bitmap {
        val result =
            createBitmap(bitmap.width, bitmap.height, bitmap.config ?: Bitmap.Config.ARGB_8888)
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
        val result =
            createBitmap(bitmap.width, bitmap.height, bitmap.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()

        val exposure = value / 100f * 1.5f
        val exposureScale = 2.0.pow(exposure.toDouble()).toFloat()

        Log.d("로그", "Exposure value: $exposureScale")

        val colorMatrix = ColorMatrix(
            floatArrayOf(
                exposureScale, 0f, 0f, 0f, 0f,
                0f, exposureScale, 0f, 0f, 0f,
                0f, 0f, exposureScale, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }

    fun applyConstruct(bitmap: Bitmap, value: Int): Bitmap {
        val result =
            createBitmap(bitmap.width, bitmap.height, bitmap.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()

        val construct = 1f + (value / 100f) // -100~100 -> 0f ~ 2f
        val translate = 128f * (1 - construct)

        Log.d("로그", "construct value: $construct, translate value : $translate")

        val colorMatrix = ColorMatrix(
            floatArrayOf(
                construct, 0f, 0f, 0f, translate,
                0f, construct, 0f, 0f, translate,
                0f, 0f, construct, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }
}