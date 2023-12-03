package com.example.moviesapp.database

import androidx.lifecycle.LiveData
import com.example.moviesapp.Model.Category
import com.example.moviesapp.Model.MovieNowPlaying
import com.example.moviesapp.Model.UserComment
import com.example.moviesapp.Network.MovieService
import retrofit2.Call

class Repository {
    private val movieService: MovieService by lazy {
        MovieService()
    }
    suspend fun getMoviePlay(): MovieNowPlaying{
        return try{
             movieService.api.getMoviePlay()
        }catch (e :Exception){
            throw e
        }
    }
    suspend fun getListCategory(): Category {
        return try{
            movieService.api.getListCategory()
        }catch (e :Exception){
            throw e
        }
    }
    suspend fun getCommentById(id:Int):UserComment{
        return try{
            movieService.api.getCommentById(id)
        }catch (e :Exception){
            throw e
        }
    }
}