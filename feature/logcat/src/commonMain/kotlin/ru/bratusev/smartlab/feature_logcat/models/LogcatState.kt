package ru.bratusev.smartlab.feature_logcat.models

import kotlinx.datetime.LocalDate
import ru.bratusev.smartlab.ui.core.models.LogcatMessageUi

data class LogcatState(
    val messages: List<LogcatMessageUi> = emptyList(),
    val isLoading: Boolean = false,

    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val selectedTypes: Set<String> = setOf("e", "d", "w")
)

sealed class Event {
    data object OnBackClicked : Event()
    data class OnTypeToggled(val type: String) : Event()
    data class OnDateRangeChanged(val startDate: LocalDate?, val endDate: LocalDate?) : Event()
}