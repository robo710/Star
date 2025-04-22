package com.sonchan.photoretouching.presentation.component

import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.sonchan.photoretouching.R
import com.sonchan.photoretouching.presentation.component.RetouchingToastUtil.SetToast
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme

object RetouchingToastUtil {
    @Composable
    fun SetToast(
        modifier: Modifier = Modifier,
        message: String,
        icon: Int,
    ) {
        Row(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = modifier
                    .size(24.dp),
                painter = painterResource(id = icon),
                contentDescription = "ToastIcon",
                tint = Color.Unspecified,
            )
            Spacer(modifier = modifier.padding(5.dp))
            Text(
                modifier = modifier,
                text = message,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

class RetouchingToast(private val context: Context) {
    fun showToast(
        message: String,
        icon: Int,
        duration: Int,
        isDarkTheme: Boolean,
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner,
        savedStateRegistryOwner: SavedStateRegistryOwner
    ) {
        fun dpToPx(dp: Int): Int {
            val density = context.resources.displayMetrics.density
            return (dp * density).toInt()
        }
        val composeView = ComposeView(context).apply {
            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeViewModelStoreOwner(viewModelStoreOwner)
            setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)
            setContent {
                PhotoRetouchingTheme(darkTheme = isDarkTheme) {
                    SetToast(
                        message = message,
                        icon = icon
                    )
                }
            }
        }

        val toast = Toast(context).apply {
            this.duration = duration
            this.view = composeView
            setGravity(
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
                0, // xOffset
                dpToPx(50 )// yOffset
            )
        }


        toast.show()

        Handler(Looper.getMainLooper()).postDelayed({
            toast.cancel()
        }, duration.toLong())
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RetouchingToastSuccessPreview() {
    PhotoRetouchingTheme {
        SetToast(
            message = "성공",
            icon = R.drawable.success_icon
        )
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RetouchingToastFailPreview() {
    PhotoRetouchingTheme {
        SetToast(
            message = "실패",
            icon = R.drawable.fail_icon
        )
    }
}