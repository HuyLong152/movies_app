package com.example.moviesapp.Model

data class MoviePopular(
    val page: Int,
    val results: List<ResultPopular>,
    val total_pages: Int,
    val total_results: Int
)