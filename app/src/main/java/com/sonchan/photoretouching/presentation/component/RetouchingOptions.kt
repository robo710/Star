package com.sonchan.photoretouching.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
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
    val density = LocalDensity.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    var contentWidth by remember { mutableStateOf(0.dp) }

    // 측정된 콘텐츠 너비를 기준으로 가운데 정렬할지 결정
    val shouldCenter = contentWidth < screenWidth

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = if (shouldCenter) Arrangement.Center else Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    with(density) {
                        contentWidth = layoutCoordinates.size.width.toDp()
                    }
                }
        ) {
            items(options) { option ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .clickable { onOptionSelected(option) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(option.icon),
                        contentDescription = option.label,
                        tint = Color.Black
                    )
                    if (selectedOption == option) {
                        Text(
                            text = option.label,
                            textAlign = TextAlign.Center
                        )
                    }
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