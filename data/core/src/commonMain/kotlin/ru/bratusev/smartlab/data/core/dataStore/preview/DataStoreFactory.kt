package ru.bratusev.smartlab.data.core.dataStore.preview

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DataStoreFactoryPreview {
    fun createDataStore(): DataStore<Preferences>
}

