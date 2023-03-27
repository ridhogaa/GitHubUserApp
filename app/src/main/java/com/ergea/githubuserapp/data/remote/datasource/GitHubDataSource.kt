package com.ergea.githubuserapp.data.remote.datasource

import com.ergea.githubuserapp.data.remote.model.DetailResponse
import com.ergea.githubuserapp.data.remote.model.ListResponseUserItem
import com.ergea.githubuserapp.data.remote.model.SearchResponse
import com.ergea.githubuserapp.data.remote.service.ApiService

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface GitHubDataSource {
    suspend fun searchUsers(q: String, sort: String, per_page: Int): SearchResponse
    suspend fun getDetailUser(username: String): DetailResponse
    suspend fun getFollowers(username: String): List<ListResponseUserItem>
    suspend fun getFollowing(username: String): List<ListResponseUserItem>
}

class GitHubDataSourceImpl(private val apiService: ApiService) : GitHubDataSource {
    override suspend fun searchUsers(q: String, sort: String, per_page: Int): SearchResponse =
        apiService.searchUsers(q, sort, per_page)

    override suspend fun getDetailUser(username: String): DetailResponse =
        apiService.getDetailUser(username)

    override suspend fun getFollowers(username: String): List<ListResponseUserItem> =
        apiService.getFollowers(username)

    override suspend fun getFollowing(username: String): List<ListResponseUserItem> =
        apiService.getFollowing(username)
}