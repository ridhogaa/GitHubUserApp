package com.ergea.githubuserapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

@Dao
interface FavoriteDao {
    @Insert
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorite_user ORDER BY id ASC")
    suspend fun getAllFavorites(): List<FavoriteEntity>

    @Query("SELECT * FROM favorite_user ORDER BY id ASC LIMIT 5")
    suspend fun getSomeFavorites(): List<FavoriteEntity>

    @Query("SELECT EXISTS(SELECT id FROM favorite_user WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}