package ru.bratusev.smartlab.ui.core.models

import org.jetbrains.compose.resources.DrawableResource

sealed class SensorCardUi(
    open val id: String,
    open val state: String,
    open val domain: String,
    open val drawableResource: DrawableResource
) {

    data class Small(
        override val id: String,
        override val state: String,
        override val domain: String,
        override val drawableResource: DrawableResource,
    ): SensorCardUi(id, state, domain, drawableResource)

    data class Medium(
        val title: String,
        override val id: String,
        override val state: String,
        override val domain: String,
        override val drawableResource: DrawableResource,
    ): SensorCardUi(id, state, domain, drawableResource)

    data class Large(
        val title: String,
        val description: String,
        override val id: String,
        override val state: String,
        override val domain: String,
        override val drawableResource: DrawableResource,
    ): SensorCardUi(id, state, domain, drawableResource)
}

