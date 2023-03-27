package com.ergea.githubuserapp.data.local.datastore

import kotlinx.coroutines.flow.Flow

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface SettingsDataStoreManagerDataSource {
    fun getTheme(): Flow<Boolean>
    suspend fun setTheme(condition: Boolean)
}

class SettingsDataStoreManagerDataSourceImpl(private val settingsDataStoreManager: SettingsDataStoreManager) :
    SettingsDataStoreManagerDataSource {
    override fun getTheme(): Flow<Boolean> = settingsDataStoreManager.getTheme
    override suspend fun setTheme(condition: Boolean) = settingsDataStoreManager.setTheme(condition)
}