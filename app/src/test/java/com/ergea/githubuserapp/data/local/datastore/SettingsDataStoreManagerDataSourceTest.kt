package com.ergea.githubuserapp.data.local.datastore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class SettingsDataStoreManagerDataSourceTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var dataStoreManagerDataSource: SettingsDataStoreManagerDataSource
    private lateinit var settingsDataStoreManager: SettingsDataStoreManager

    @Before
    fun setUp() {
        settingsDataStoreManager = mock()
        dataStoreManagerDataSource =
            SettingsDataStoreManagerDataSourceImpl(settingsDataStoreManager)
    }

    @Test
    fun getTheme() = runBlocking {
        val correct = mockk<Flow<Boolean>>()
        Mockito.`when`(settingsDataStoreManager.getTheme).thenReturn(correct)
        val response = dataStoreManagerDataSource.getTheme()
        assertEquals(response, correct)
    }

    @Test
    fun setTheme() = runBlocking {
        val correct = mockk<Unit>()
        Mockito.`when`(settingsDataStoreManager.setTheme(any())).thenReturn(correct)
        val response = dataStoreManagerDataSource.setTheme(any())
        assertEquals(response, correct)
    }
}