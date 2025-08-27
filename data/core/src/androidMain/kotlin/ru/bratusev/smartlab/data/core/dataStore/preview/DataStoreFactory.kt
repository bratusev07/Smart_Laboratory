package ru.bratusev.smartlab.data.core.dataStore.preview

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class DataStoreFactoryPreview() {
    actual fun createDataStore(): DataStore<Preferences> {
        println("creating android dataStore")
        return createDataStore(
            producePath = { "" }
        )
    }
}

