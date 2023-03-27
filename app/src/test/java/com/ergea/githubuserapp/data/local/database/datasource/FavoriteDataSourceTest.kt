package com.ergea.githubuserapp.data.local.database.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ergea.githubuserapp.data.local.database.dao.FavoriteDao
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity
import io.mockk.mockk
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

class FavoriteDataSourceTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var dataSourceImpl: FavoriteDataSource
    private lateinit var favoriteDao: FavoriteDao

    @Before
    fun setUp() {
        favoriteDao = mock()
        dataSourceImpl = FavoriteDataSourceImpl(favoriteDao)
    }

    @Test
    fun addFavorite() = runBlocking {
        val correct = mockk<Unit>()
        Mockito.`when`(favoriteDao.addFavorite(any())).thenReturn(correct)
        val response = dataSourceImpl.addFavorite(any())
        assertEquals(response, correct)
    }

    @Test
    fun removeFavorite() = runBlocking {
        val correct = mockk<Unit>()
        Mockito.`when`(favoriteDao.removeFavorite(any())).thenReturn(correct)
        val response = dataSourceImpl.removeFavorite(any())
        assertEquals(response, correct)
    }

    @Test
    fun getAllFavorites() = runBlocking {
        val correct = mockk<List<FavoriteEntity>>()
        Mockito.`when`(favoriteDao.getAllFavorites()).thenReturn(correct)
        val response = dataSourceImpl.getAllFavorites()
        assertEquals(response, correct)
    }

    @Test
    fun getSomeFavorites() = runBlocking {
        val correct = mockk<List<FavoriteEntity>>()
        Mockito.`when`(favoriteDao.getSomeFavorites()).thenReturn(correct)
        val response = dataSourceImpl.getSomeFavorites()
        assertEquals(response, correct)
    }

    @Test
    fun isFavorite() = runBlocking {
        assertEquals(dataSourceImpl.isFavorite(10), favoriteDao.isFavorite(10))
    }
}