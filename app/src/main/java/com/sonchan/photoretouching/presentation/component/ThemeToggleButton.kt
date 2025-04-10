package com.sonchan.photoretouching.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.sonchan.photoretouching.R
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme

@Composable
fun ThemeToggleButton(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
){
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onToggleTheme) {
            val icon = if (isDarkTheme) R.drawable.dark_mode_icon else R.drawable.light_mode_icon
            val contentDesc = if (isDarkTheme) "다크 모드" else "라이트 모드"

            Icon(
                painter = painterResource(id = icon),
                contentDescription = contentDesc,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThemeToggleButtonPreview(){
    PhotoRetouchingTheme {
        ThemeToggleButton(
            isDarkTheme = false,
            onToggleTheme = {}
        )
    }
}