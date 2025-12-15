package ru.bratusev.smartlab.data.core.local_storage.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DataStoreFactory {
    fun createDataStore(): DataStore<Preferences>
}

