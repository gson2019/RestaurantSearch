package com.alltrails.restaurantsearch.data

sealed class ApiResult {
    data class Success(val data: List<ResultsItem>): ApiResult()

    data class Error(val message: String?): ApiResult()

    data class Loading(val message: String?): ApiResult()
}




