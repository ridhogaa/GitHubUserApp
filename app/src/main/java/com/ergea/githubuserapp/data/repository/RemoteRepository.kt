package com.ergea.githubuserapp.data.repository

import com.ergea.githubuserapp.data.remote.datasource.GitHubDataSource
import com.ergea.githubuserapp.data.remote.model.DetailResponse
import com.ergea.githubuserapp.data.remote.model.ListResponseUserItem
import com.ergea.githubuserapp.data.remote.model.SearchResponse
import com.ergea.githubuserapp.wrapper.Resource

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface RemoteRepository {
    suspend fun searchUsers(q: String, sort: String, per_page: Int): Resource<SearchResponse>
    suspend fun getDetailUser(username: String): Resource<DetailResponse>
    suspend fun getFollowers(username: String): Resource<List<ListResponseUserItem>>
    suspend fun getFollowing(username: String): Resource<List<ListResponseUserItem>>
}

class RemoteRepositoryImpl(private val gitHubDataSource: GitHubDataSource) : RemoteRepository {
    override suspend fun searchUsers(
        q: String,
        sort: String,
        per_page: Int
    ): Resource<SearchResponse> = proceed {
        gitHubDataSource.searchUsers(q, sort, per_page)
    }

    override suspend fun getDetailUser(username: String): Resource<DetailResponse> = proceed {
        gitHubDataSource.getDetailUser(username)
    }

    override suspend fun getFollowers(username: String): Resource<List<ListResponseUserItem>> =
        proceed {
            gitHubDataSource.getFollowers(username)
        }

    override suspend fun getFollowing(username: String): Resource<List<ListResponseUserItem>> =
        proceed {
            gitHubDataSource.getFollowing(username)
        }

    private suspend fun <T> proceed(coroutine: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(coroutine.invoke())
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }
}