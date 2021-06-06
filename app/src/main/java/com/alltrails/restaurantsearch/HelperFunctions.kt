package com.alltrails.restaurantsearch

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.alltrails.restaurantsearch.data.OpeningHours
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory


const val MAXIMUM_PRICE_LEVEL = 5

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

 fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}