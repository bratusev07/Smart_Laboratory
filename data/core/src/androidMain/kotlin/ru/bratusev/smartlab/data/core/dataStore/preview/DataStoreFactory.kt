package ru.bratusev.smartlab.data.core.dataStore.preview

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ru.bratusev.smartlab.data.core.dataStore.dataStoreFileName

actual class DataStoreFactoryPreview(val context: Context) {
    actual fun createDataStore(): DataStore<Preferences> {
        println("creating android dataStore")
        return createDataStore(
            producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath + "_preview"}
        )
    }
}

