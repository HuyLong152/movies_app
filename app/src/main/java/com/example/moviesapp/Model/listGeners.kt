package com.example.moviesapp.Model

data class listGeners(
    val id: Int,
    val page: Int,
    val results: List<ResultSearch>,
    val total_pages: Int,
    val total_results: Int
)