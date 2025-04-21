package com.sonchan.photoretouching.presentation.component

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.sonchan.photoretouching.R
import com.sonchan.photoretouching.presentation.viewmodel.ThemeViewModel

@Composable
fun BackOnPressed(
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var backPressedTime = 0L // 뒤로가기 버튼을 눌렀던 시간을 저장하는 변수
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current

    BackHandler(enabled = true) {
        // 만약 전에 뒤로가기 버튼 누른 시간과 특정한 시간 만큼 차이가 나지 않으면 앱종료.
        if(System.currentTimeMillis() - backPressedTime <= 1000L) {
            (context as Activity).finish() // 앱 종료
        } else {
            // 특정한 시간 이상으로 차이가 난다면 토스트로 한 번 더 버튼을 누르라고 알림
            RetouchingToast(context).showToast(
                message = "한번 더 클릭시 앱이 종료됩니다.",
                icon = R.drawable.splash_icon,
                duration = Toast.LENGTH_SHORT,
                isDarkTheme = isDarkTheme,
                lifecycleOwner = lifecycleOwner,
                viewModelStoreOwner = viewModelStoreOwner!!,
                savedStateRegistryOwner = savedStateRegistryOwner
            )
        }
        // 뒤로가기 버튼을 눌렀던 시간을 저장
        backPressedTime = System.currentTimeMillis()
    }
}