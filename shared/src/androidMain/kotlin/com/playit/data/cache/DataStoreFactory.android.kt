package com.playit.data.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

actual object DataStoreFactory {
    private lateinit var applicationContext: Context

    fun initialize(context: Context) {
        this.applicationContext = context.applicationContext
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cache_prefs")

    actual fun createDataStore(): DataStore<Preferences> {
        return applicationContext.dataStore
    }
}
