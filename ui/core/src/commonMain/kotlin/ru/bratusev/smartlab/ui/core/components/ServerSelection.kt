package ru.bratusev.smartlab.ui.core.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.ServerSelectionUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

private val itemHeight = 36.dp
private val contentPadding = 8.dp

@Composable
fun ServerSelectionDropDown(
    modifier: Modifier = Modifier,
    uiData: ServerSelectionUi,
    onSelect: (url: String) -> Unit,
    onExpand: () -> Unit = {},
    onClose: () -> Unit = {},
    onDelete: (url: String) -> Unit
) {
    val maxScreenHeight = rememberWindowHeight()
    val height by animateDpAsState(
        targetValue = if (uiData.expanded) {
            min(
                maxScreenHeight / 6,
                itemHeight * (uiData.serverList.size + 1) + 2 * contentPadding + uiData.serverList.size * 8.dp
            )
        } else {
            itemHeight + contentPadding * 2
        }
    )
    val interactionSource = remember { MutableInteractionSource() }
    Box(modifier = Modifier.requiredHeight(itemHeight + contentPadding * 2).zIndex(1f)) {
        if (uiData.expanded) {
            Box(
                modifier = Modifier.requiredHeight(maxScreenHeight).fillMaxWidth()
                    .clickable(interactionSource = interactionSource, indication = null) {
                        onClose()
                    })
        }
        LazyColumn(
            modifier = modifier.requiredHeight(height).background(
                MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.large
            ).clip(shape = MaterialTheme.shapes.large),
            contentPadding = PaddingValues(contentPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (uiData.expanded) {
                items(uiData.serverList.keys.toList()) { url ->
                    if (uiData.serverList.isNotEmpty()) {
                        ServerItem(
                            url = url,
                            name = uiData.serverList.getOrElse(url) { "Ошибка" },
                            selected = uiData.currentServerUrl == url,
                            onClick = {
                                onSelect(url)
                            },
                            onDelete = { onDelete(url) },
                            isDeletable = true,
                            showSelectionArrows = true
                        )
                    } else {
                        Text("Список серверов пуст")
                    }
                }
                item {
                    ServerItem(
                        url = "Добавить",
                        name = "+",
                        selected = false,
                        onClick = {},
                        onDelete = {},
                        isDeletable = false,
                        showSelectionArrows = false
                    )
                }
            } else {
                item {
                    ServerItem(
                        url = uiData.currentServerUrl ?: "",
                        name = uiData.serverList.getOrElse(
                            uiData.currentServerUrl ?: ""
                        ) { "Ошибка" },
                        selected = true,
                        onClick = onExpand,
                        onDelete = {},
                        isDeletable = false,
                        showSelectionArrows = false,
                    )
                }
            }
        }
    }
}

@Composable
private fun ServerItem(
    modifier: Modifier = Modifier,
    url: String,
    name: String,
    selected: Boolean,
    showSelectionArrows: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    isDeletable: Boolean
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
            false
        })

    val swipeProgress = dismissState.progress

    val backgroundColor by animateColorAsState(
        targetValue = if (swipeProgress > 0f) {
            MaterialTheme.colorScheme.error.copy(alpha = 0.2f + 0.3f * swipeProgress)
        } else {
            Transparent
        }, animationSpec = spring()
    )

    val iconColor by animateColorAsState(
        targetValue = if (swipeProgress > 0f) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0f), animationSpec = spring()
    )

    val iconScale by animateFloatAsState(
        targetValue = if (swipeProgress > 0f) 1.2f else 1f, animationSpec = spring()
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromEndToStart = isDeletable,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(backgroundColor, shape = MaterialTheme.shapes.medium)
                    .padding(end = 16.dp), contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = iconColor,
                    modifier = Modifier.size(20.dp).scale(iconScale)
                )
            }
        },
        content = {
            Row(
                modifier = modifier.height(itemHeight).fillMaxWidth().background(
                    if (selected) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.secondary, shape = MaterialTheme.shapes.medium
                ).clickable { onClick() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selected && showSelectionArrows) Text(">")

                Text(
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    text = url.replaceBefore("://", "").replace("://", ""),
                    color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (selected) FontWeight.ExtraBold else null,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    text = name,
                    color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (selected) FontWeight.ExtraBold else null,
                    textAlign = TextAlign.Center
                )
                if (selected && showSelectionArrows) Text("<")
            }
        })
}

@Composable
private fun rememberWindowHeight(): Dp {
    val windowInfo = LocalWindowInfo.current
    return remember(windowInfo) { windowInfo.containerSize.height.dp }
}


@Composable
@Preview(
    showBackground = true
)
private fun PreviewServerSelectionExpanded() {
    AppTheme {
        ServerSelectionDropDown(
            uiData = ServerSelectionUi(
                serverList = mapOf("255.255.255.255" to "Preview", "254.254.254.254" to "Preview2"),
                currentServerUrl = "254.254.254.254",
                expanded = true
            ), onSelect = {}, onDelete = {})
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun PreviewServerSelectionNotExpanded() {
    AppTheme {
        ServerSelectionDropDown(
            uiData = ServerSelectionUi(
                serverList = mapOf("255.255.255.255" to "Preview", "254.254.254.254" to "Preview2"),
                currentServerUrl = "254.254.254.254",
                expanded = false
            ), onSelect = {}, onDelete = {})
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun PreviewServerItem() {
    AppTheme {
        ServerItem(
            url = "255.255.255.255",
            name = "Preview",
            selected = false,
            onClick = {},
            onDelete = {},
            isDeletable = true,
            showSelectionArrows = true
        )
    }
}
