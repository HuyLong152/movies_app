package com.example.moviesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovie::class], version = 1,exportSchema = false)
abstract class MovieDatabase:RoomDatabase() {
    abstract fun movieDao():MovieDao
    companion object{
        @Volatile
        private var instance:MovieDatabase? = null
        fun getInstance(context: Context):MovieDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context,MovieDatabase::class.java,"MovieDB").build()
            }
            return instance!!
        }
    }
}