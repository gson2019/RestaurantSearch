package com.alltrails.restaurantsearch.ui.map

import android.app.Activity
import android.content.Context
import android.view.View
import com.alltrails.restaurantsearch.R
import com.alltrails.restaurantsearch.data.ResultsItem
import com.alltrails.restaurantsearch.getPriceLevelString
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.restaurant_info_window.view.*

class RestaurantInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(p0: Marker): View? {
        val mInfoView = (context as Activity).layoutInflater.inflate(R.layout.restaurant_info_window, null)
        var restaurantInfo: ResultsItem? = p0.tag as ResultsItem?

        with(mInfoView) {
            restaurantInfo?.let {
                restaurantNameTv.text = it.name
                Glide.with(restaurantImg.context)
                    .load(it.icon)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(restaurantImg)
                ratingBar.rating = it.rating.toFloat()
                reviewCount.text = "(${it.userRatingsTotal})"
                statusTv.text = context.getString(R.string.restaurant_status, it.priceLevel.getPriceLevelString(), it.businessStatus)
            }
        }
        return mInfoView
    }

    override fun getInfoContents(p0: Marker): View? {
        return null
    }
}