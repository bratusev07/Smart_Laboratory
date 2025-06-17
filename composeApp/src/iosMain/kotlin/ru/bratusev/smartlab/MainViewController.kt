package ru.bratusev.smartlab

import androidx.compose.ui.window.ComposeUIViewController
import ru.bratusev.smartlab.di.initKoin


fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }