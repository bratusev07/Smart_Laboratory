package ru.bratusev.smartlab.data.core.local_storage.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class DataStoreFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDataStore(): DataStore<Preferences> {
        println("creating ios dataStore")
        return createDataStore(
            producePath = {
                val documentDirectory: NSURL? = NSFileManager.Companion.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                requireNotNull(documentDirectory).path + "/${dataStoreFileName}"
            })
    }
}