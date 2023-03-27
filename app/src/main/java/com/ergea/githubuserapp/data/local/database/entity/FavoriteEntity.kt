package com.ergea.githubuserapp.data.local.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "login")
    var login: String?,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean?
) : Parcelable