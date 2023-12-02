package com.example.moviesapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ListFavorite")
data class FavoriteMovie(
    @PrimaryKey(autoGenerate = true)
    val Id:Int,
    val IdMovie:Int
)