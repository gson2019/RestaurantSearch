package com.alltrails.restaurantsearch.data

sealed class ApiResult

class Success(val data: Any): ApiResult()

class Error(val message: String?): ApiResult()
