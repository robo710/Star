package com.sonchan.photoretouching.util

import android.content.Context
import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageExposureFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHighlightShadowFilter

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
        val intensity = value / 100f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(GPUImageHighlightShadowFilter(intensity, 0f))
        return gpuImage.bitmapWithFilterApplied
    }


    fun applyShadow(context: Context, bitmap: Bitmap, value: Int): Bitmap {
        val intensity = value / 100f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(GPUImageHighlightShadowFilter(0f, intensity))
        return gpuImage.bitmapWithFilterApplied
    }
}