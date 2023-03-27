package com.ergea.githubuserapp.data.repository

import com.ergea.githubuserapp.data.local.database.datasource.FavoriteDataSource
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity
import com.ergea.githubuserapp.data.local.datastore.SettingsDataStoreManagerDataSource
import com.ergea.githubuserapp.wrapper.Resource
import kotlinx.coroutines.flow.Flow

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

interface LocalRepository {
    fun getTheme(): Flow<Boolean>
    suspend fun setTheme(condition: Boolean)
    suspend fun addFavorite(favorite: FavoriteEntity)
    suspend fun removeFavorite(favorite: FavoriteEntity)
    suspend fun getAllFavorites(): Resource<List<FavoriteEntity>>
    suspend fun getSomeFavorites(): Resource<List<FavoriteEntity>>
    suspend fun isFavorite(id: Int): Boolean
}

class LocalRepositoryImpl(
    private val settingsDataStoreManagerDataSource: SettingsDataStoreManagerDataSource,
    private val favoriteDataSource: FavoriteDataSource
) : LocalRepository {
    override fun getTheme(): Flow<Boolean> = settingsDataStoreManagerDataSource.getTheme()

    override suspend fun setTheme(condition: Boolean) =
        settingsDataStoreManagerDataSource.setTheme(condition)

    override suspend fun addFavorite(favorite: FavoriteEntity) =
        favoriteDataSource.addFavorite(favorite)

    override suspend fun removeFavorite(favorite: FavoriteEntity) =
        favoriteDataSource.removeFavorite(favorite)

    override suspend fun getAllFavorites(): Resource<List<FavoriteEntity>> = proceed {
        favoriteDataSource.getAllFavorites().map {
            FavoriteEntity(
                it.id,
                it.login,
                it.avatarUrl,
                it.isFavorite
            )
        }
    }

    override suspend fun getSomeFavorites(): Resource<List<FavoriteEntity>> = proceed {
        favoriteDataSource.getSomeFavorites().map {
            FavoriteEntity(
                it.id,
                it.login,
                it.avatarUrl,
                it.isFavorite
            )
        }
    }

    override suspend fun isFavorite(id: Int): Boolean = favoriteDataSource.isFavorite(id)

    private suspend fun <T> proceed(coroutine: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(coroutine.invoke())
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }
}