package com.alltrails.restaurantsearch.network

import com.google.android.gms.maps.model.LatLng

class RestaurantSearchRequest(
    val latLng: LatLng,
    val radius: Int,
    val type: String = "restaurant",
    val keyword: String
) {
    fun parseRequest() {

    }
}