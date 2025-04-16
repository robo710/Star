package com.sonchan.photoretouching.domain.model

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Retouching : Screen("retouching")
}