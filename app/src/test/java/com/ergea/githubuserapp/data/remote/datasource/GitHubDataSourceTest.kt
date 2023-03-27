package com.ergea.githubuserapp.data.remote.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ergea.githubuserapp.data.remote.model.DetailResponse
import com.ergea.githubuserapp.data.remote.model.ListResponseUserItem
import com.ergea.githubuserapp.data.remote.model.SearchResponse
import com.ergea.githubuserapp.data.remote.service.ApiService
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

class GitHubDataSourceTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var dataSource: GitHubDataSource
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        apiService = mock()
        dataSource = GitHubDataSourceImpl(apiService)
    }

    @Test
    fun searchUsers() = runBlocking {
        val correct = mockk<SearchResponse>()
        Mockito.`when`(apiService.searchUsers(any(), any(), any())).thenReturn(correct)
        val response = dataSource.searchUsers("a", "q", 10)
        assertEquals(response, correct)
    }

    @Test
    fun getDetailUser() = runBlocking {
        val correct = mockk<DetailResponse>()
        Mockito.`when`(apiService.getDetailUser(any())).thenReturn(correct)
        val response = dataSource.getDetailUser("ridhogaa")
        assertEquals(response, correct)
    }

    @Test
    fun getFollowers() = runBlocking {
        val correct = mockk<List<ListResponseUserItem>>()
        Mockito.`when`(apiService.getFollowers(any())).thenReturn(correct)
        val response = dataSource.getFollowers("ridhogaa")
        assertEquals(response, correct)
    }

    @Test
    fun getFollowing() = runBlocking {
        val correct = mockk<List<ListResponseUserItem>>()
        Mockito.`when`(apiService.getFollowing(any())).thenReturn(correct)
        val response = dataSource.getFollowing("ridhogaa")
        assertEquals(response, correct)
    }
}