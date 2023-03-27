package com.ergea.githubuserapp.data.local.database.datasource

import com.ergea.githubuserapp.data.local.database.dao.FavoriteDao
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface FavoriteDataSource {
    suspend fun addFavorite(favorite: FavoriteEntity)
    suspend fun removeFavorite(favorite: FavoriteEntity)
    suspend fun getAllFavorites(): List<FavoriteEntity>
    suspend fun getSomeFavorites(): List<FavoriteEntity>
    suspend fun isFavorite(id: Int): Boolean
}

class FavoriteDataSourceImpl(private val favoriteDao: FavoriteDao) : FavoriteDataSource {
    override suspend fun addFavorite(favorite: FavoriteEntity) = favoriteDao.addFavorite(favorite)
    override suspend fun removeFavorite(favorite: FavoriteEntity) =
        favoriteDao.removeFavorite(favorite)

    override suspend fun getAllFavorites(): List<FavoriteEntity> = favoriteDao.getAllFavorites()
    override suspend fun getSomeFavorites(): List<FavoriteEntity> = favoriteDao.getSomeFavorites()
    override suspend fun isFavorite(id: Int): Boolean = favoriteDao.isFavorite(id)
}