package com.ergea.githubuserapp.data.remote.service

import com.ergea.githubuserapp.data.remote.model.DetailResponse
import com.ergea.githubuserapp.data.remote.model.ListResponseUserItem
import com.ergea.githubuserapp.data.remote.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface ApiService {

    @GET("/search/users?")
    suspend fun searchUsers(
        @Query("q") q: String,
        @Query("s") sort: String,
        @Query("per_page") per_page: Int
    ): SearchResponse

    @GET("/users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): DetailResponse

    @GET("/users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String
    ): List<ListResponseUserItem>

    @GET("/users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String
    ): List<ListResponseUserItem>

}