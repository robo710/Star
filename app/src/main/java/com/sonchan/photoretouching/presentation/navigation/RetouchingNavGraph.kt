package com.sonchan.photoretouching.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sonchan.photoretouching.presentation.screen.RetouchingRoute
import com.sonchan.photoretouching.presentation.screen.SplashScreen

object Screen {
    const val SPLASH = "splash"
    const val RETOUCH = "retouch"
}

@Composable
fun RetouchingNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.RETOUCH
    ){
        composable(Screen.SPLASH) {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(Screen.RETOUCH){
                        popUpTo(Screen.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.RETOUCH){
            RetouchingRoute()
        }
    }
}