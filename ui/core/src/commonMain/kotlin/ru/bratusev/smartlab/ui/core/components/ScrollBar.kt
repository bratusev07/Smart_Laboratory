package ru.bratusev.smartlab.ui.core.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.verticalScrollbar(
    state: LazyListState,
    scrollbarWidth: Dp = 8.dp,
    scrollbarColor: Color = Color.Gray
): Modifier = composed {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val animationDuration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = animationDuration)
    )

    drawWithContent {
        drawContent()

        val visibleItemsInfo = state.layoutInfo.visibleItemsInfo
        if (visibleItemsInfo.isEmpty()) {
            return@drawWithContent
        }

        // Calculate the total content height and the viewport height
        val totalContentHeight = state.layoutInfo.totalItemsCount * visibleItemsInfo.first().size.toFloat()
        val viewportHeight = state.layoutInfo.viewportSize.height.toFloat()

        // Don't draw scrollbar if content fits within the viewport
        if (totalContentHeight <= viewportHeight) {
            return@drawWithContent
        }

        // Calculate the size of the scrollbar thumb
        val thumbHeight = (viewportHeight / totalContentHeight) * viewportHeight

        // Calculate the position of the scrollbar thumb
        val averageItemSize = totalContentHeight / state.layoutInfo.totalItemsCount
        val scrollOffset = state.firstVisibleItemIndex * averageItemSize + state.firstVisibleItemScrollOffset
        val scrollPercentage = scrollOffset / (totalContentHeight - viewportHeight)
        val thumbOffsetY = scrollPercentage * (viewportHeight - thumbHeight)


        drawRect(
            color = scrollbarColor,
            topLeft = Offset(this.size.width - scrollbarWidth.toPx(), thumbOffsetY),
            size = Size(scrollbarWidth.toPx(), thumbHeight),
            alpha = alpha
        )
    }
}