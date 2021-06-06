package com.alltrails.restaurantsearch.network

import com.alltrails.restaurantsearch.BuildConfig
import com.alltrails.restaurantsearch.data.RestaurantSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlaceApi {
    @GET("json")
    suspend fun searchNearbyRestaurants(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String = "restaurant",
        @Query("keyword") keyword: String,
        @Query("key") key: String = MAPS_API_KEY
    ): RestaurantSearchResponse

    @GET("json")
    suspend fun searchNearbyRestaurants(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("key") key: String = MAPS_API_KEY,
        @Query("pagetoken") pageToken: String
    ): RestaurantSearchResponse


    companion object {
        const val BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/"
        const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY
    }
}