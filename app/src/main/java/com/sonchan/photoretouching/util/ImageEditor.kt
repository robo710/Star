package com.sonchan.photoretouching.util

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sonchan.photoretouching.gpu.HighlightFilter
import com.sonchan.photoretouching.gpu.ShadowFilter
import com.sonchan.photoretouching.gpu.TintFilter
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageExposureFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageWhiteBalanceFilter

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

    fun applySaturation(context: Context, bitmap: Bitmap, value: Int): Bitmap {
        val intensity = (value + 100) / 100f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(GPUImageSaturationFilter(intensity))
        return gpuImage.bitmapWithFilterApplied
    }

    fun applyTint(context: Context, bitmap: Bitmap, value: Int): Bitmap {
        val intensity = value / 200f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(TintFilter(intensity))
        return gpuImage.bitmapWithFilterApplied
    }

    fun applyTemperature(context: Context, bitmap: Bitmap, value: Int): Bitmap{
        val intensity = 5000f + value * 50f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(GPUImageWhiteBalanceFilter(intensity, 0f))
        return gpuImage.bitmapWithFilterApplied
    }

    fun applySharpness(context: Context, bitmap: Bitmap, value: Int): Bitmap{
        val intensity = value / 25f
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(GPUImageSharpenFilter(intensity))
        return gpuImage.bitmapWithFilterApplied
    }
}