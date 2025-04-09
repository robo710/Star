package com.sonchan.photoretouching.presentation.component

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.sonchan.photoretouching.R
import com.sonchan.photoretouching.presentation.component.RetouchingToastUtil.SetToast

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
                    color = Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(12.dp)
                )
                .border(1.dp, Color(0xFF868686), RoundedCornerShape(12.dp))
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "ToastIcon",
                tint = Color.Unspecified,
            )
            Spacer(modifier = modifier.padding(5.dp))
            Text(
                modifier = modifier,
                text = message,
                textAlign = TextAlign.Center
            )
        }
    }
}

class RetouchingToast(private val context: Context) {
    fun showToast(
        message: String,
        icon: Int,
        duration: Int,
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
                SetToast(
                    message = message,
                    icon = icon
                )
            }
        }

        Toast(context).apply {
            this.duration = duration
            this.view = composeView
            setGravity(
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
                0, // xOffset
                dpToPx(50 )// yOffset
            )
            show()
        }
    }
}

@Preview
@Composable
fun RetouchingToastPreview() {
    SetToast(
        message = "성공",
        icon = R.drawable.success_icon
    )
}