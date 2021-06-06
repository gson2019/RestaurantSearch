package com.alltrails.restaurantsearch.data

/**
 * @Package com.alltrails.restaurantsearch.data
 * @FileName Result
 * @Date 6/5/21, 6:21 PM
 * @Author Created by fengchengding
 * @Description RestaurantSearch
 */
sealed class ApiResult

class Success(val data: Any): ApiResult()

class Error(val message: String?): ApiResult()
