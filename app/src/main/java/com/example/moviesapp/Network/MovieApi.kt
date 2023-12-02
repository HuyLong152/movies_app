package com.example.moviesapp.Network

import androidx.lifecycle.LiveData
import com.example.moviesapp.Model.Casts
import com.example.moviesapp.Model.Category
import com.example.moviesapp.Model.DetailAMovie
import com.example.moviesapp.Model.MovieByName
import com.example.moviesapp.Model.MovieNowPlaying
import com.example.moviesapp.Model.MoviePopular
import com.example.moviesapp.Model.Videos
import com.example.moviesapp.Model.listGeners
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/now_playing?api_key=e8f7794c71ed1ef464624c36278f4b9f")
    suspend fun getMoviePlay():MovieNowPlaying
    @GET("genre/movie/list?api_key=e8f7794c71ed1ef464624c36278f4b9f")
    suspend fun getListCategory():Category
    @GET("movie/popular?api_key=e8f7794c71ed1ef464624c36278f4b9f")
    fun getListPopular():Call<MoviePopular>

    @GET("movie/{id}?api_key=e8f7794c71ed1ef464624c36278f4b9f")
    fun getDetailById(@Path("id") id:Int):Call<DetailAMovie>
    @GET("movie/{id}/casts?api_key=e8f7794c71ed1ef464624c36278f4b9f")
    fun getCastById(@Path("id") id:Int):Call<Casts>
    @GET ("genre/{id}/movies?api_key=e8f7794c71ed1ef464624c36278f4b9f")
    fun getListGenre(@Path("id") id:Int):Call<listGeners>

    @GET("search/movie")
    fun getMovieByName(
        @Query("api_key") apiKey: String = "e8f7794c71ed1ef464624c36278f4b9f",
        @Query("query") txtName: String
    ): Call<MovieByName>

    @GET("movie/{id}/videos")
    fun getVideoById(
        @Path("id") id:Int,
        @Query("api_key") api_key:String = "e8f7794c71ed1ef464624c36278f4b9f"
    ):Call<Videos>
}