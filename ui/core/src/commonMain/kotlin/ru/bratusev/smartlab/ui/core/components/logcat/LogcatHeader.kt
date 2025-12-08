package ru.bratusev.smartlab.ui.core.components.logcat

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LogcatHeader(
    selectedTypes: Set<String>,
    onTypeToggled: (String) -> Unit
) {
    Surface(
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilterIconToggle(
                    type = "e",
                    isSelected = selectedTypes.contains("e"),
                    onClick = { onTypeToggled("e") }
                )
                FilterIconToggle(
                    type = "w",
                    isSelected = selectedTypes.contains("w"),
                    onClick = { onTypeToggled("w") }
                )
                FilterIconToggle(
                    type = "d",
                    isSelected = selectedTypes.contains("d"),
                    onClick = { onTypeToggled("d") }
                )
                FilterIconToggle(
                    type = "i",
                    isSelected = selectedTypes.contains("i"),
                    onClick = { onTypeToggled("i") }
                )
            }
        }
    }
}

@Composable
private fun FilterIconToggle(
    type: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val style = getLogStyle(type)
    // Animate scale when toggled
    val scale by animateFloatAsState(if (isSelected) 1.1f else 1f)
    // Animate color
    val backgroundColor by animateColorAsState(
        if (isSelected) style.color else MaterialTheme.colorScheme.surfaceVariant
    )
    val iconColor by animateColorAsState(
        if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .size(36.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = style.icon,
            contentDescription = type,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}