package ru.bratusev.smartlab.data.core.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module

expect class DataStoreFactory {
    fun createDataStore(): DataStore<Preferences>
}

