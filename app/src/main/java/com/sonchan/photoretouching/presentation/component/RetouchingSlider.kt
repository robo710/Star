package com.sonchan.photoretouching.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun RetouchingSlider(
    modifier: Modifier = Modifier,
    value: Int,
    valueRange: IntRange,
    listState: LazyListState,
    onValueChanged: (Int) -> Unit
) {
    val tickSpacing = 8.dp
    val tickWidth = 2.dp

    val density = LocalDensity.current
    val itemSpacingPx = with(density) { tickSpacing.toPx() }
    val tickWidthPx = with(density) { tickWidth.toPx() }
    val itemSizePx = tickWidthPx + itemSpacingPx

    // 가운데 정렬을 위한 양쪽 padding 계산
    val contentPadding = with(density) {
        ((LocalConfiguration.current.screenWidthDp.dp.toPx() / 2) - (tickWidth.toPx() / 2)).toInt()
    }

    LaunchedEffect(value) {
        val index = value - valueRange.first
        listState.animateScrollToItem(index, scrollOffset = -contentPadding)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (value > 0) "+$value" else "$value",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            // 가운데 기준선
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )

            // 틱 마크 리스트
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(horizontal = with(density) { contentPadding.toDp() }),
                horizontalArrangement = Arrangement.spacedBy(tickSpacing),
                content = {
                    items(valueRange.count()) { index ->
                        val tickValue = valueRange.first + index
                        Box(
                            modifier = Modifier
                                .width(tickWidth)
                                .height(if (tickValue % 10 == 0) 24.dp else 16.dp)
                                .background(
                                    if (tickValue == value) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                        )
                    }
                }
            )
        }
    }

    // 스크롤 감지 → 값 갱신
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                val calculatedIndex = (index + ((offset + itemSizePx / 2) / itemSizePx).toInt()).coerceIn(0, valueRange.count() - 1)
                val newValue = valueRange.first + calculatedIndex
                if (newValue != value) {
                    onValueChanged(newValue)
                }
            }
    }
}


@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RetouchingSlidePreview() {
    PhotoRetouchingTheme {
        val previewState = rememberLazyListState()
        var currentValue by remember { mutableStateOf(0) }
        RetouchingSlider(
            value = currentValue,
            valueRange = -100..100,
            listState = previewState,
            onValueChanged = { currentValue = it }
        )
    }
}
