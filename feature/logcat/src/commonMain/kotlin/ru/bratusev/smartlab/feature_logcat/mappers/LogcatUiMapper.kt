package ru.bratusev.smartlab.feature_logcat.mappers

import ru.bratusev.smartlab.domain.core.model.LogcatMessage
import ru.bratusev.smartlab.ui.core.models.LogcatMessageUi

fun LogcatMessage.mapToUi() = LogcatMessageUi(
    title = title ?: "Empty title",
    description = description ?: "Empty description",
    time = time ?: "Empty time",
    date = date ?: "Empty date",
    type = type ?: "Empty type"
)