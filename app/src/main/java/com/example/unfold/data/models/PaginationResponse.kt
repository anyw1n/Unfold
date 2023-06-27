package com.example.unfold.data.models

data class PaginationResponse<T>(
    val results: List<T>,
)
