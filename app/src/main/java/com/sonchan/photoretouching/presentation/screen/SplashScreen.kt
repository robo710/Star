package com.sonchan.photoretouching.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.sonchan.photoretouching.R
import com.sonchan.photoretouching.presentation.component.DarkThemeDevicePreview
import com.sonchan.photoretouching.presentation.component.DevicePreviews
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onNavigateToMain: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "fade"
    )

    LaunchedEffect(Unit) {
        delay(300L)

        visible = true
        delay(1500L)

        visible = false
        delay(1500L)

        onNavigateToMain()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.splash_icon),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(160.dp)
                    .alpha(alpha)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Star",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.alpha(alpha)
            )
        }
    }
}

@DevicePreviews
@DarkThemeDevicePreview
@Composable
fun SplashScreenPreview() {
    PhotoRetouchingTheme {
        SplashScreen(
            onNavigateToMain = {}
        )
    }
}
