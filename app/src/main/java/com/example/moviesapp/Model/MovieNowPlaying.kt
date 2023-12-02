package com.example.moviesapp.Model

data class MovieNowPlaying(
    val dates: Dates,
    val page: Int,
    val results: List<ResultPlaying>,
    val total_pages: Int,
    val total_results: Int
)