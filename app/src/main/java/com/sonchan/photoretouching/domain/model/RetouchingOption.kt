package com.sonchan.photoretouching.domain.model

import androidx.annotation.DrawableRes
import com.sonchan.photoretouching.R

enum class RetouchingOption(
    val label: String,
    @DrawableRes val icon: Int,
    val range: IntRange = -100..100,
    val defaultValue: Int = 0
) {
    LIGHT_BALANCE("라이트 밸런스", R.drawable.light_balance_icon),
    BRIGHTNESS("밝기", R.drawable.brightness_icon),
    EXPOSURE("노출", R.drawable.exposure_icon),
    CONTRAST("대비", R.drawable.contrast_icon),
    HIGHLIGHT("하이라이트", R.drawable.highlight_icon),
    SHADOW("그림자", R.drawable.shadow_icon),
    SATURATION("채도", R.drawable.saturation_icon),
    TINT("틴트", R.drawable.tint_icon),
    TEMPERATURE("색온도", R.drawable.temperature_icon),
    SHARPNESS("선명도", R.drawable.sharpness_icon),
    CLARITY("명료도", R.drawable.clarity_icon)
}