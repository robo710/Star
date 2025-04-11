package com.sonchan.photoretouching.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sonchan.photoretouching.R
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme

@Composable
fun RetouchingSlider(
    modifier: Modifier = Modifier,
    value: Int,
    valueRange: IntRange,
    listState: LazyListState,
    onValueChanged: (Int) -> Unit,
    tickInterval: Int,
    onResetValue: () -> Unit,
) {
    val tickList = IntProgression.fromClosedRange(valueRange.first, valueRange.last, tickInterval).toList()
    val centerValue = (valueRange.first + valueRange.last) / 2

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 기준선
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        )

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$value",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Box(
                modifier = Modifier
                    .weight(3f),
                contentAlignment = Alignment.Center
            ) {
                LazyRow(
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(tickList) { tickValue ->
                        val isEmphasized = tickValue == valueRange.first || tickValue == valueRange.last || tickValue == centerValue
                        val height = if (isEmphasized) 16.dp else 12.dp
                        val width = if (isEmphasized) 3.dp else 2.dp

                        Box(
                            modifier = Modifier
                                .width(width)
                                .height(height)
                                .background(
                                    if (tickValue == value) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                .clickable { onValueChanged(tickValue) }
                        )
                    }
                }
            }

            IconButton(
                onClick = { onResetValue() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.reset_icon),
                    contentDescription = "Reset Setting",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChanged(it.toInt()) },
            valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
            modifier = Modifier
                .fillMaxWidth(0.8f),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        )
    }

}



@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RetouchingSlidePreview() {
    PhotoRetouchingTheme {
        val previewState = rememberLazyListState()
        RetouchingSlider(
            value = 0,
            valueRange = -100..100,
            listState = previewState,
            onValueChanged = {},
            tickInterval = 10,
            onResetValue = {}
        )
    }
}
