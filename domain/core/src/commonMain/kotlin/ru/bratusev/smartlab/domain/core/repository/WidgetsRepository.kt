package ru.bratusev.smartlab.domain.core.repository

import ru.bratusev.smartlab.domain.core.model.CustomWidget

interface WidgetsRepository {
    suspend fun saveWidgets(widgets: List<CustomWidget>)
    suspend fun getWidgets(): List<CustomWidget>
}
