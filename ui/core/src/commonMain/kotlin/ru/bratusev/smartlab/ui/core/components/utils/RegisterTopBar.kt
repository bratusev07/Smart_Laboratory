package ru.bratusev.smartlab.ui.core.components.utils

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Stable
class TopBarState {
    private val stack = mutableStateListOf<String>()
    private val contents = mutableStateMapOf<String, @Composable RowScope.() -> Unit>()

    val currentContent: (@Composable RowScope.() -> Unit)?
        get() = stack.lastOrNull()?.let { contents[it] }

    fun register(id: String, content: @Composable RowScope.() -> Unit) {
        contents[id] = content
        if (!stack.contains(id)) stack.add(id)
    }

    fun unregister(id: String) {
        contents.remove(id)
        stack.remove(id)
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun RegisterTopBar(
    topBarState: TopBarState,
    content: @Composable RowScope.() -> Unit
) {
    val currentContent by rememberUpdatedState(content)

    val screenId = rememberSaveable { Uuid.random().toString() }

    DisposableEffect(screenId) {
        topBarState.register(screenId) { currentContent() }

        onDispose {
            topBarState.unregister(screenId)
        }
    }
}