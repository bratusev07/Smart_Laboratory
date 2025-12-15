package ru.bratusev.smartlab.data.core.local_storage.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class DataStoreFactory(val context: Context) {
    actual fun createDataStore(): DataStore<Preferences> {
        println("creating android dataStore")
        return createDataStore(
            producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
        )
    }
}