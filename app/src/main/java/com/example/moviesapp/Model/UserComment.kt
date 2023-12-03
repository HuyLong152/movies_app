package com.example.moviesapp.Model

data class UserComment(
    val id: Int,
    val page: Int,
    val results: List<ResultComment>,
    val total_pages: Int,
    val total_results: Int
)