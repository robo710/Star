package com.sonchan.photoretouching.util

import android.content.Context
import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageExposureFilter

object ImageEditor {
    fun applyBrightness(context: Context, bitmap: Bitmap, value: Int): Bitmap {
        val intensity = value / 100f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(GPUImageBrightnessFilter(intensity))
        return gpuImage.bitmapWithFilterApplied
    }

    fun applyExposure(context: Context, bitmap: Bitmap, value: Int): Bitmap {
        val intensity = value / 100f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(GPUImageExposureFilter(intensity))
        return gpuImage.bitmapWithFilterApplied
    }

    fun applyContrast(context: Context, bitmap: Bitmap, value: Int): Bitmap {
        val intensity = (value + 100) / 100f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(GPUImageContrastFilter(intensity))
        return gpuImage.bitmapWithFilterApplied
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