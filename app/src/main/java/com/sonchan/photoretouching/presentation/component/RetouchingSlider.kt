package com.sonchan.photoretouching.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sonchan.photoretouching.R
import com.sonchan.photoretouching.ui.theme.PhotoRetouchingTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun RetouchingSlider(
    modifier: Modifier = Modifier,
    value: Int,
    valueRange: IntRange,
    listState: LazyListState,
    tickInterval: Int,
    onValueChanged: (Int) -> Unit,
    onResetValue: () -> Unit,
) {
    val tickList = (valueRange.first..valueRange.last).toList()
    val coroutineScope = rememberCoroutineScope()
    val spacing = 1.dp
    val itemWidth = 6.dp

    val spacingPx = with(LocalDensity.current) { spacing.toPx() }
    val itemWidthPx = with(LocalDensity.current) { itemWidth.toPx() }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
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

            BoxWithConstraints(
                modifier = Modifier.weight(3f),
                contentAlignment = Alignment.Center
            ) {
                val screenWidth = maxWidth
                val centerPadding = remember(screenWidth) {
                    PaddingValues(horizontal = screenWidth / 2 - itemWidth / 2)
                }

                LazyRow(
                    state = listState,
                    contentPadding = centerPadding,
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(tickList) { index, tickValue ->
                        val isEmphasized = tickValue % tickInterval == 0
                        val tickBarWidth = if (isEmphasized) 3.dp else 2.dp
                        val tickBarHeight = if (isEmphasized) 20.dp else 10.dp

                        Box(
                            modifier = Modifier
                                .width(itemWidth)
                                .height(tickBarHeight),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(tickBarWidth)
                                    .height(tickBarHeight)
                                    .background(
                                        if (tickValue == value) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    .then(
                                        if (isEmphasized) Modifier.clickable {
                                            coroutineScope.launch {
                                                listState.animateScrollToItem(index)
                                                onValueChanged(tickValue)
                                            }
                                        } else Modifier
                                    )
                            )
                        }
                    }
                }
                LaunchedEffect(listState) {
                    // 슬라이더 초기화 시 한 번만 스크롤을 맞추기
                    val index = tickList.indexOf(value).takeIf { it >= 0 } ?: 0
                    listState.animateScrollToItem(index)
                }

                Box(
                    Modifier
                        .width(2.dp)
                        .height(24.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                )
            }

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        val centerIdx = tickList.indexOf(0).takeIf { it >= 0 } ?: 0
                        listState.animateScrollToItem(centerIdx)
                    }
                    onResetValue()
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.reset_icon),
                    contentDescription = "Reset Setting",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    // 스크롤 따라 값 업데이트
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex +
                listState.firstVisibleItemScrollOffset / (itemWidthPx + spacingPx) }
            .map { it.roundToInt().coerceIn(0, tickList.lastIndex) }
            .distinctUntilChanged()
            .collect { onValueChanged(tickList[it]) }
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
