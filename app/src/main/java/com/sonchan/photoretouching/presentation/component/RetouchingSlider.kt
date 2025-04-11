package com.sonchan.photoretouching.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 기준선
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        )

        // 틱 마커와 텍스트 및 리셋 버튼을 포함하는 Row
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 왼쪽 수치 표시 (고정된 크기로 설정)
            Box(
                modifier = modifier
                    .wrapContentWidth()
                    .padding(end = 25.dp) // 틱 마커와의 간격 조정
            ) {
                Text(
                    text = "$value",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // 틱 마커들
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            ) {
                items(tickList) { tickValue ->
                    Box(
                        modifier = modifier
                            .width(2.dp)
                            .height(12.dp)
                            .background(
                                if (tickValue == value) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                    )
                }
            }

            // 오른쪽 리셋 버튼
            IconButton(onClick = { onResetValue() }) {
                Icon(
                    painter = painterResource(R.drawable.reset_icon),
                    contentDescription = "Reset Setting",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // 슬라이더
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChanged(it.toInt()) },
            valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 8.dp),
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
