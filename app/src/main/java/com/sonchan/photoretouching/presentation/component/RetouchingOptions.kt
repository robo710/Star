package com.sonchan.photoretouching.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sonchan.photoretouching.domain.model.RetouchingOption
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme

@Composable
fun RetouchingOptions(
    modifier: Modifier = Modifier,
    options: List<RetouchingOption>,
    selectedOption: RetouchingOption?,
    onOptionSelected: (RetouchingOption) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(options) { option ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .clickable{ onOptionSelected(option) }
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(option.icon),
                    tint = Color.Black,
                    contentDescription = option.label,
                    modifier = modifier.padding(4.dp)
                )
                if (option == selectedOption) {
                    Text(
                        text = option.label,
                        textAlign = TextAlign.Center
                    )
                } else{
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RetouchingOptionsPreview(){
    PhotoRetouchingTheme {
        RetouchingOptions(
            options = RetouchingOption.values().toList(),
            selectedOption = RetouchingOption.BRIGHTNESS,
            onOptionSelected = {}
        )
    }
}