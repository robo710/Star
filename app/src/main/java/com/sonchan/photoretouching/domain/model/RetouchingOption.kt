package com.sonchan.photoretouching.domain.model

import androidx.annotation.DrawableRes
import com.sonchan.photoretouching.R

enum class RetouchingOption(val label: String, @DrawableRes val icon: Int) {
    LIGHT_BALANCE("라이트 밸런스", R.),
    BRIGHTNESS("밝기", R.drawable.ic_brightness),
    EXPOSURE("노출", R.drawable.ic_exposure),
    CONTRAST("대비", R.drawable.ic_contrast),
    HIGHLIGHT("하이라이트", R.drawable.ic_highlight),
    SHADOW("그림자", R.drawable.ic_shadow),
    SATURATION("채도", R.drawable.ic_saturation),
    TINT("틴트", R.drawable.ic_tint),
    TEMPERATURE("색온도", R.drawable.ic_temperature),
    SHARPNESS("선명도", R.drawable.ic_sharpness),
    CLARITY("명료도", R.drawable.ic_clarity)
}