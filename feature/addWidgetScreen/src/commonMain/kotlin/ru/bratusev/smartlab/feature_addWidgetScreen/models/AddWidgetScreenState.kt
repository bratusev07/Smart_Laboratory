package ru.bratusev.smartlab.feature_addWidgetScreen.models

import ru.bratusev.smartlab.domain.core.model.CustomWidget
import kotlin.reflect.KClass

data class AddWidgetScreenState(
    val screenName: String = "AddWidgetScreenScreen",

    )

sealed class Event {
    data class OnSaveWidget(val widget: KClass<out CustomWidget>) : Event()
}