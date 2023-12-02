package com.example.moviesapp.database

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.Model.Category
import com.example.moviesapp.Model.MovieNowPlaying
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call

class MovieViewModel(context: Context):ViewModel() {
    private val movieReponsitory: Repository by lazy{
        Repository()
    }

    private var movieDatabase = MovieDatabase.getInstance(context)
    private var movieDao = movieDatabase.movieDao()
    fun insertMovie(movie:FavoriteMovie){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.InsertMovie(movie)
        }
    }
    fun removeMovie(movie:FavoriteMovie){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.DeleteMovie(movie)
        }
    }

    private var _getMovie = movieDao.getAllMovie()
    val getmovie:LiveData<List<FavoriteMovie>>
        get() = _getMovie

    fun isExistId(IdMovie:Int):LiveData<Int>{
           return movieDao.checkExistId(IdMovie)
    }

    fun removeMovieById(IdMovie:Int){
            movieDao.deleteById(IdMovie)
    }


    private val _listCategory = MutableLiveData<Category>()
    val listCategory :LiveData<Category> get() = _listCategory!!


    private val _listPlaying = MutableLiveData<MovieNowPlaying>()
    val listPlaying :LiveData<MovieNowPlaying> get() = _listPlaying!!
    init{
        viewModelScope.launch {
            _listPlaying.value = movieReponsitory.getMoviePlay()
            _listCategory.value = movieReponsitory.getListCategory()
        }
    }
}