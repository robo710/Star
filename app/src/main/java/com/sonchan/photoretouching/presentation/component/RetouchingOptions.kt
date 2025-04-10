package com.sonchan.photoretouching.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
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
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(options) { option ->
            val isSelected = selectedOption == option

            val scale by animateFloatAsState(if (isSelected) 1.2f else 1f)
            val tintColor by animateColorAsState(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
            val interactionSource = remember { MutableInteractionSource() }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) {
                        onOptionSelected(option)
                    }
            ) {
                Icon(
                    painter = painterResource(option.icon),
                    contentDescription = option.label,
                    tint = tintColor,
                    modifier = modifier
                        .size(32.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale
                        )
                )
                if (isSelected) {
                    Text(
                        text = option.label,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelSmall
                    )
                } else{
                    Text( text = "" )
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