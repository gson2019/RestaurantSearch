package com.alltrails.restaurantsearch

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.core.content.ContextCompat
import com.alltrails.restaurantsearch.data.OpeningHours
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng


const val MAXIMUM_PRICE_LEVEL = 5
const val LATEST_LOCATION = "latest location"
fun Int.getPriceLevelString(): String {
  return if (this < MAXIMUM_PRICE_LEVEL) {
      "$".repeat(this)
  } else {
      "$".repeat(MAXIMUM_PRICE_LEVEL)
  }
}

fun OpeningHours.parseRestaurantStatus(): String {
    return if(this.openNow) {
        "Open"
    } else {
        "Closed"
    }
}

fun LatLng.format() = "${this.latitude},${this.longitude}"
fun Location.format() = "${this.latitude},${this.longitude}"

fun SharedPreferences.updateLocation(latestLocation: String) {
    this.edit().putString(LATEST_LOCATION, latestLocation).apply()
}

