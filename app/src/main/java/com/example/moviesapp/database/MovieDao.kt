package com.example.moviesapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert
    fun InsertMovie(movie:FavoriteMovie)
    @Delete
    fun DeleteMovie(movie:FavoriteMovie)

    @Query("select * from ListFavorite")
    fun getAllMovie():LiveData<List<FavoriteMovie>>

    @Query("SELECT COUNT(*) FROM ListFavorite WHERE IdMovie = :idCheck")
    fun checkExistId(idCheck:Int):LiveData<Int>

    @Query("DELETE FROM ListFavorite WHERE IdMovie = :idCheck")
    fun deleteById(idCheck:Int)
}