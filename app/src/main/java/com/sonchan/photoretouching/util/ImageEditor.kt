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

    fun applyHighlight(bitmap: Bitmap, value: Int): Bitmap {
        val highlightFactor = value / 150f
        val width = bitmap.width
        val height = bitmap.height
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val pixels = IntArray(width * height)  // 픽셀 배열 초기화
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)  // 원본 이미지의 픽셀 데이터를 가져옴

        for (i in pixels.indices) {
            val pixel = pixels[i]

            val r = (pixel shr 16 and 0xFF).toFloat()
            val g = (pixel shr 8 and 0xFF).toFloat()
            val b = (pixel and 0xFF).toFloat()

            // 밝기 계산: 밝은 영역만 선택
            val brightness = (r + g + b) / 2.5
            val factor = if (brightness > 200) highlightFactor else 0f  // 밝은 영역만 강조

            // 하이라이트 효과 적용: 밝은 부분만 강조
            val newR = (r + (255 - r) * factor).coerceIn(0f, 255f)
            val newG = (g + (255 - g) * factor).coerceIn(0f, 255f)
            val newB = (b + (255 - b) * factor).coerceIn(0f, 255f)

            // 새로운 색상 계산
            val newColor = (0xFF shl 24) or
                    (newR.toInt() shl 16) or
                    (newG.toInt() shl 8) or
                    newB.toInt()

            pixels[i] = newColor  // 픽셀 배열에 새로운 색상을 설정
        }

        result.setPixels(pixels, 0, width, 0, 0, width, height)  // 결과 이미지를 픽셀 배열로 설정
        return result
    }

}