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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.bratusev.smartlab.ui.core.models.ServerSelectionUi
import ru.bratusev.smartlab.ui.core.theme.AppTheme
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.add_new_server
import smartlaboratory.ui.core.generated.resources.cant_be_empty
import smartlaboratory.ui.core.generated.resources.login
import smartlaboratory.ui.core.generated.resources.login_must_be_filled_when_password_filled
import smartlaboratory.ui.core.generated.resources.password
import smartlaboratory.ui.core.generated.resources.save_server
import smartlaboratory.ui.core.generated.resources.select_server
import smartlaboratory.ui.core.generated.resources.server_name
import smartlaboratory.ui.core.generated.resources.server_url

private val ITEM_HEIGHT = 56.dp
private val ITEMS_SPACED_BY = 8.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServerSelectionDropDown(
    modifier: Modifier = Modifier,
    uiData: ServerSelectionUi,
    onSelect: (ServerSelectionUi.ServerInfoUi) -> Unit,
    onExpand: () -> Unit = {},
    onClose: () -> Unit = {},
    onDelete: (ServerSelectionUi.ServerInfoUi) -> Unit,
    onAddServer: (url: String, name: String, login: String, password: String) -> Unit,
) {
    val density = LocalDensity.current
    var dropdownWidth by remember { mutableStateOf(0.dp) }

    val transitionState = remember { MutableTransitionState(uiData.expanded) }
    transitionState.targetState = uiData.expanded

    val orderedItems = remember(uiData.serverList, uiData.currentServer) {
        val list = uiData.serverList.toMutableList()
        val selectedItem = uiData.currentServer

        // We use object equality here, which handles duplicate URLs correctly
        if (selectedItem != null && list.contains(selectedItem)) {
            list.remove(selectedItem)
            list.add(0, selectedItem)
        }
        list.toList()
    }

    val windowHeight = rememberWindowHeight()
    val contentHeight =
        (orderedItems.size + 1) * ITEM_HEIGHT + (orderedItems.size + 1) * ITEMS_SPACED_BY
    val expandedHeight = min(contentHeight, windowHeight * 0.5f)

    val heightAnim by animateDpAsState(
        targetValue = if (uiData.expanded) expandedHeight else ITEM_HEIGHT,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "ContainerHeight"
    )

    Box(
        modifier = modifier.fillMaxWidth().height(ITEM_HEIGHT)
            .onGloballyPositioned { dropdownWidth = with(density) { it.size.width.toDp() } }) {

        ServerItemContent(
            url = uiData.currentServer?.url ?: "",
            name = uiData.currentServer?.name ?: stringResource(Res.string.select_server),
            login = uiData.currentServer?.login ?: "",
            selected = true,
            isExpanded = false,
            onClick = onExpand,
            modifier = Modifier.matchParentSize(),
        )

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
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    tonalElevation = 8.dp
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(ITEMS_SPACED_BY),
                        contentPadding = PaddingValues(
                            top = if (uiData.currentServer == null) 8.dp else 0.dp,
                            bottom = 8.dp,
                        )
                    ) {
                        items(
                            items = orderedItems,
                            key = { "${it.url}_${it.name}_${it.login}" }
                        ) { serverInfo ->
                            val isSelected = serverInfo == uiData.currentServer

                            Box(modifier = Modifier.animateItem()) {
                                SwipeableServerItemWrapper(
                                    isDeletable = true,
                                    onDelete = {
                                        onDelete(serverInfo)
                                    }) {
                                    ServerItemContent(
                                        url = serverInfo.url,
                                        name = serverInfo.name,
                                        login = serverInfo.login,
                                        selected = isSelected,
                                        isExpanded = isSelected && uiData.expanded,
                                        onClick = {
                                            if (isSelected) onClose() else onSelect(serverInfo)
                                        })
                                }
                            }
                        }

                        item {
                            Box(modifier = Modifier.animateItem()) {
                                AddServerItem(
                                    onAddServer = onAddServer
                                )
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
    login: String,
    selected: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                text = if (login.isNotEmpty()) "$name - $login" else name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (url.isNotEmpty()) {
                Text(
                    text = url.replaceBefore("://", "").replace("://", ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.7f),
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
                tint = contentColor
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
private fun AddServerItem(onAddServer: (url: String, name: String, login: String, password: String) -> Unit) {
    var isAddServerDialogOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.height(ITEM_HEIGHT).fillMaxWidth().clip(MaterialTheme.shapes.medium)
            .clickable { isAddServerDialogOpen = true }.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(Res.string.add_new_server),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
    AddServerDialog(
        isOpen = isAddServerDialogOpen,
        onClose = { isAddServerDialogOpen = false },
        onConfirm = { url, name, login, password ->
            isAddServerDialogOpen = false
            onAddServer(url, name, login, password)
        }
    )
}

@Composable
private fun AddServerDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onClose: () -> Unit,
    onConfirm: (url: String, name: String, login: String, password: String) -> Unit
) {
    var url by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showUrlError by remember { mutableStateOf(false) }
    var showNameError by remember { mutableStateOf(false) }
    var showLoginError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }

    if (isOpen) {
        Dialog(
            onDismissRequest = onClose,
        ) {
            Card(modifier = modifier, shape = MaterialTheme.shapes.large) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = url,
                        label = { Text(text = stringResource(Res.string.server_url)) },
                        supportingText = {
                            if (showUrlError) {
                                Text(text = stringResource(Res.string.cant_be_empty))
                            }
                        },
                        isError = showUrlError,
                        onValueChange = { url = it },
                        singleLine = true
                    )
                    TextField(
                        value = name,
                        label = { Text(text = stringResource(Res.string.server_name)) },
                        supportingText = {
                            if (showNameError) {
                                Text(text = stringResource(Res.string.cant_be_empty))
                            }
                        },
                        isError = showNameError,
                        onValueChange = { name = it },
                        singleLine = true
                    )
                    TextField(
                        value = login,
                        label = { Text(text = stringResource(Res.string.login)) },
                        supportingText = {
                            if (showLoginError) {
                                Text(text = stringResource(Res.string.login_must_be_filled_when_password_filled))
                            }
                        },
                        isError = showLoginError,
                        onValueChange = { login = it },
                        singleLine = true
                    )
                    TextField(
                        value = password,
                        label = { Text(text = stringResource(Res.string.password)) },
                        onValueChange = { password = it },
                        singleLine = true
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        onClick = {
                            showUrlError = url.isEmpty()
                            showNameError = name.isEmpty()
                            showLoginError = login.isEmpty() && password.isNotEmpty()
                            if (!showNameError && !showUrlError && !showLoginError) {
                                onConfirm(url, name, login, password)
                                url = ""; name = ""; login = ""; password = ""
                            }
                        }
                    ) {
                        Text(text = stringResource(Res.string.save_server))
                    }
                }
            }
        }
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
@Preview(showBackground = true)
private fun PreviewServerSelectionExpanded() {
    val sampleList = listOf(
        ServerSelectionUi.ServerInfoUi("255.255.255.255", "Preview", "", ""),
        ServerSelectionUi.ServerInfoUi("254.254.254.254", "Preview2", "", "")
    )
    AppTheme {
        ServerSelectionDropDown(
            uiData = ServerSelectionUi(
                serverList = sampleList,
                currentServer = sampleList[1], // Passed Object
                expanded = true
            ),
            onSelect = {},
            onDelete = { _ -> },
            onAddServer = { _, _, _, _ -> }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewServerSelectionNotExpanded() {
    val sampleList = listOf(
        ServerSelectionUi.ServerInfoUi("255.255.255.255", "Preview", "", ""),
        ServerSelectionUi.ServerInfoUi("254.254.254.254", "Preview2", "", "")
    )
    AppTheme {
        ServerSelectionDropDown(
            uiData = ServerSelectionUi(
                serverList = sampleList,
                currentServer = sampleList[1], // Passed Object
                expanded = false
            ),
            onSelect = {},
            onDelete = { _ -> },
            onAddServer = { _, _, _, _ -> }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewServerItem() {
    AppTheme {
        ServerItemContent(
            url = "255.255.255.255",
            name = "Preview",
            selected = false,
            onClick = {},
            isExpanded = false,
            login = "PreviewLogin"
        )
    }
}