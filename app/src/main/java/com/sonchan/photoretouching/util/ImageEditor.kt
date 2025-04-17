package com.sonchan.photoretouching.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.Log
import kotlin.math.pow
import androidx.core.graphics.createBitmap
import com.sonchan.photoretouching.gpu.BrightnessFilter
import com.sonchan.photoretouching.gpu.ExposureFilter
import com.sonchan.photoretouching.gpu.HighlightFilter
import com.sonchan.photoretouching.gpu.ShadowFilter
import jp.co.cyberagent.android.gpuimage.GPUImage

object ImageEditor {
    fun applyBrightness(context: Context, bitmap: Bitmap, value: Int): Bitmap {
        val brightness = value / 300f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(BrightnessFilter(brightness))
        return gpuImage.bitmapWithFilterApplied
    }

    fun applyExposure(context: Context, bitmap: Bitmap, value: Int): Bitmap {
        val exposure = value / 200f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(ExposureFilter(exposure))
        return gpuImage.bitmapWithFilterApplied
    }

    fun applyConstruct(bitmap: Bitmap, value: Int): Bitmap {
        val result =
            createBitmap(bitmap.width, bitmap.height, bitmap.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()

        val construct = 1f + (value / 100f)
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

    fun applyHighlight(context: Context, bitmap: Bitmap, value: Int): Bitmap {
        val intensity = value / 150f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(HighlightFilter(intensity))
        return gpuImage.bitmapWithFilterApplied
    }


    fun applyShadow(context: Context, bitmap: Bitmap, value: Int): Bitmap {
        val intensity = value / 300f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(ShadowFilter(intensity))
        return gpuImage.bitmapWithFilterApplied
    }
}