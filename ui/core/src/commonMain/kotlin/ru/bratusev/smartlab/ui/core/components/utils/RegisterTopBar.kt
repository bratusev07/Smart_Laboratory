package ru.bratusev.smartlab.ui.core.components.utils

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun RegisterTopBar(
    setTopBar: (@Composable RowScope.() -> Unit) -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentContent by rememberUpdatedState(content)

    DisposableEffect(lifecycleOwner) {
        setTopBar(currentContent)

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> setTopBar(currentContent)
                Lifecycle.Event.ON_PAUSE -> setTopBar {}
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            setTopBar {}
        }
    }

    LaunchedEffect(content) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            setTopBar(content)
        }
    }
}