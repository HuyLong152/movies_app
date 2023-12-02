package com.example.moviesapp.Model

data class MovieByName(
    val page: Int,
    val results: List<ResultByName>,
    val total_pages: Int,
    val total_results: Int
)