package com.sonchan.photoretouching.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sonchan.photoretouching.domain.model.Screen
import com.sonchan.photoretouching.presentation.screen.RetouchingRoute
import com.sonchan.photoretouching.presentation.screen.SplashScreen

@Composable
fun RetouchingNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ){
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(Screen.Retouching.route){
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Retouching.route){
            RetouchingRoute()
        }
    }
}