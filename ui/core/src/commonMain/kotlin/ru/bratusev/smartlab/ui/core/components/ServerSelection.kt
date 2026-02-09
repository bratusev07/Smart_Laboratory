package ru.bratusev.smartlab.ui.core.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.ServerSelectionUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme

private val ITEM_HEIGHT = 56.dp
private val ITEMS_SPACED_BY = 8.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServerSelectionDropDown(
    modifier: Modifier = Modifier,
    uiData: ServerSelectionUi,
    onSelect: (url: String) -> Unit,
    onExpand: () -> Unit = {},
    onClose: () -> Unit = {},
    onDelete: (url: String) -> Unit
) {
    val density = LocalDensity.current
    var dropdownWidth by remember { mutableStateOf(0.dp) }

    val transitionState = remember { MutableTransitionState(uiData.expanded) }
    transitionState.targetState = uiData.expanded

    val orderedItems = remember(uiData.serverList, uiData.currentServerUrl) {
        val list = uiData.serverList.keys.toMutableList()
        val selected = uiData.currentServerUrl
        if (selected != null && list.contains(selected)) {
            list.remove(selected)
            list.add(0, selected)
        }
        list.toList()
    }

    val windowHeight = rememberWindowHeight()
    val contentHeight = (orderedItems.size + 1) * ITEM_HEIGHT + orderedItems.size * ITEMS_SPACED_BY
    val expandedHeight = min(contentHeight, windowHeight * 0.5f)

    val heightAnim by animateDpAsState(
        targetValue = if (uiData.expanded) expandedHeight else ITEM_HEIGHT,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "ContainerHeight"
    )

    Box(
        modifier = modifier.fillMaxWidth().height(ITEM_HEIGHT)
            .onGloballyPositioned { dropdownWidth = with(density) { it.size.width.toDp() } }) {
        // Anchor
        ServerItemContent(
            url = uiData.currentServerUrl ?: "",
            name = uiData.serverList[uiData.currentServerUrl] ?: "Select Server",
            selected = true,
            isExpanded = false,
            onClick = onExpand,
            modifier = Modifier.matchParentSize()
        )

        // Popup
        if (transitionState.currentState || transitionState.targetState) {
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, 0),
                onDismissRequest = onClose,
                properties = PopupProperties(focusable = true, clippingEnabled = false)
            ) {
                Surface(
                    modifier = Modifier.width(dropdownWidth).height(heightAnim),
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.secondaryContainer, // <--- CHANGED: Container color from First Code
                    tonalElevation = 8.dp
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(ITEMS_SPACED_BY),
                        contentPadding = PaddingValues(
                            top = if (uiData.currentServerUrl == null) 8.dp else 0.dp,
                            bottom = 8.dp,
                        )
                    ) {
                        items(
                            items = orderedItems, key = { it }) { url ->
                            val isSelected = url == uiData.currentServerUrl

                            Box(modifier = Modifier.animateItem()) {
                                SwipeableServerItemWrapper(
                                    isDeletable = true, onDelete = { onDelete(url) }) {
                                    ServerItemContent(
                                        url = url,
                                        name = uiData.serverList[url] ?: "Unknown",
                                        selected = isSelected,
                                        isExpanded = isSelected && uiData.expanded,
                                        onClick = { if (isSelected) onClose() else onSelect(url) })
                                }
                            }
                        }

                        item {
                            Box(modifier = Modifier.animateItem()) {
                                AddServerItem()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServerItemContent(
    url: String,
    name: String,
    selected: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // <--- CHANGED: Logic strictly from First Code --->
    val backgroundColor = if (selected) MaterialTheme.colorScheme.secondaryContainer
    else MaterialTheme.colorScheme.secondary

    val contentColor = if (selected) MaterialTheme.colorScheme.onSecondaryContainer
    else MaterialTheme.colorScheme.onSecondary
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier.height(ITEM_HEIGHT).fillMaxWidth()
            .then(if (!selected) Modifier.padding(horizontal = 8.dp) else Modifier)
            .clip(MaterialTheme.shapes.medium).background(backgroundColor)
            .padding(horizontal = 16.dp)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor, // Apply color here
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (url.isNotEmpty()) {
                Text(
                    text = url.replaceBefore("://", "").replace("://", ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.7f), // Apply color here
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        val rotation by animateFloatAsState(if (isExpanded) 180f else 0f, label = "rot")
        if (selected) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                "Expand",
                Modifier.rotate(rotation),
                tint = contentColor // Apply color here
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableServerItemWrapper(
    isDeletable: Boolean, onDelete: () -> Unit, content: @Composable () -> Unit
) {
    if (!isDeletable) {
        content(); return
    }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        })

    // Swipe Logic (Colors preserved from previous working iteration as requested "Except animation colors")
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) MaterialTheme.colorScheme.error
                else Transparent, label = "color"
            )
            Box(
                modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.medium)
                    .background(color).padding(end = 24.dp), contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.onError)
            }
        },
        content = { content() })
}

@Composable
private fun AddServerItem() {
    Row(
        modifier = Modifier.height(ITEM_HEIGHT).fillMaxWidth().clip(MaterialTheme.shapes.medium)
            .clickable { }.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(8.dp))
        Text(
            "Add New Server",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun rememberWindowHeight(): Dp {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    return remember(
        windowInfo, density
    ) { with(density) { windowInfo.containerSize.height.toDp() } }
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
                serverList = mapOf(
                    "255.255.255.255" to "Preview",
                    "254.254.254.254" to "Preview2",
                    "254.254.254.253" to "Preview3",
                    "254.254.254.254" to "Preview4",
                    "254.254.254.255" to "Preview5",
                    "254.254.254.256" to "Preview6",
                ), currentServerUrl = "254.254.254.254", expanded = false
            ), onSelect = {}, onDelete = {})
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun PreviewServerItem() {
    AppTheme {
        ServerItemContent(
            url = "255.255.255.255",
            name = "Preview",
            selected = false,
            onClick = {},
            isExpanded = false,
        )
    }
}
