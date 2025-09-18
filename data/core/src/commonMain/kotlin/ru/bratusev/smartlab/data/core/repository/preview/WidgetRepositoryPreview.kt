package ru.bratusev.smartlab.data.core.repository.preview

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.serialization.json.Json
import ru.bratusev.smartlab.data.core.mapper.toEntity
import ru.bratusev.smartlab.domain.core.model.CustomWidget
import ru.bratusev.smartlab.domain.core.repository.WidgetsRepository

class WidgetRepositoryPreview(private val dataStore: DataStore<Preferences>) : WidgetsRepository {
    override suspend fun saveWidgets(widgets: List<CustomWidget>) {
        val widgetsEntities = widgets.map {
            it.toEntity()
        }
        val jsonString = Json.encodeToString(widgetsEntities)
        println("✅ Preview: Widgets save jsonString result $jsonString")
        dataStore.edit { preferences ->
            preferences[WIDGETS_KEY] = jsonString
        }
    }

    override suspend fun getWidgets(): List<CustomWidget> {
        val result = buildList {
            add(
                CustomWidget.SingleSensor(
                    sensorId = "Id1",
                    id = 1
                )
            )
            add(
                CustomWidget.SensorsList(
                    sensorsIds = listOf("Id1", "Id2"),
                    id = 2
                )
            )
        }
        println("✅ Preview: Widgets read result $result")
        return result
    }

    private companion object {
        val WIDGETS_KEY = stringPreferencesKey("auth_token")
    }
}