package com.sonchan.photoretouching.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sonchan.photoretouching.domain.model.ImageFormat
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme

@Composable
fun ImageFormatDropDown(
    selectedFormat: ImageFormat,
    isFormatMenuExpanded: Boolean,
    onExpandFormatMenu: () -> Unit,
    onDismissFormatMenu: () -> Unit,
    onSelectFormat: (ImageFormat) -> Unit,
){
    Box {
        Text(
            text = selectedFormat.name,
            modifier = Modifier
                .clickable { onExpandFormatMenu() }
                .padding(8.dp)
        )
        DropdownMenu(
            expanded = isFormatMenuExpanded,
            onDismissRequest = { onDismissFormatMenu() }
        ) {
            ImageFormat.entries.forEach { format ->
                DropdownMenuItem(
                    text = { Text(format.name) },
                    onClick = {
                        onSelectFormat(format)
                        onDismissFormatMenu()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageFormatDropDownPreview(){
    PhotoRetouchingTheme {
        ImageFormatDropDown(
            selectedFormat = ImageFormat.JPG,
            isFormatMenuExpanded = false,
            onExpandFormatMenu = {},
            onDismissFormatMenu = {},
            onSelectFormat = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImageFormatDropDownExpandedPreview(){
    PhotoRetouchingTheme {
        ImageFormatDropDown(
            selectedFormat = ImageFormat.JPG,
            isFormatMenuExpanded = true,
            onExpandFormatMenu = {},
            onDismissFormatMenu = {},
            onSelectFormat = {}
        )
    }
}