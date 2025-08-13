package ru.bratusev.smartlab.feature_logcat.models

import ru.bratusev.smartlab.ui.core.models.LogcatMessageUi

data class LogcatState(
    val messages: List<LogcatMessageUi> = emptyList<LogcatMessageUi>()
)

sealed class Event {
    data object OnBackClicked: Event()
}