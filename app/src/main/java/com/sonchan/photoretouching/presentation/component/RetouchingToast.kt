package com.sonchan.photoretouching.presentation.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
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
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(size = 12.dp)
                )
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "ToastIcon",
                tint = Color.Unspecified
            )
            Text(text = message)
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
        val composeView = ComposeView(context).apply {
            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeViewModelStoreOwner(viewModelStoreOwner)
            setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)
            setContent {
                RetouchingToastUtil.SetToast(
                    message = message,
                    icon = icon
                )
            }
        }

        Toast(context).apply {
            this.duration = duration
            this.view = composeView
            show()
        }
    }
}

@Preview
@Composable
fun RetouchingToastPreview() {
    SetToast(
        message = "안녕",
        icon = R.drawable.add_icon
    )
}