package com.alltrails.restaurantsearch.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantSearchResponse(

	@field:SerializedName("next_page_token")
	val nextPageToken: String,

//	@field:SerializedName("html_attributions")
//	val htmlAttributions: List<Any>,

	@field:SerializedName("results")
	val results: List<ResultsItem>,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class OpeningHours(

	@field:SerializedName("open_now")
	val openNow: Boolean
) : Parcelable

@Parcelize
data class Northeast(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
) : Parcelable

@Parcelize
data class PlusCode(

	@field:SerializedName("compound_code")
	val compoundCode: String,

	@field:SerializedName("global_code")
	val globalCode: String
) : Parcelable

@Parcelize
data class Geometry(

	@field:SerializedName("viewport")
	val viewport: Viewport,

	@field:SerializedName("location")
	val location: Location
) : Parcelable

@Entity(tableName = "favoriteRestaurants")
@Parcelize
data class ResultsItem(

	@field:SerializedName("types")
	val types: List<String>,

	@field:SerializedName("business_status")
	val businessStatus: String,

	@field:SerializedName("icon")
	val icon: String,

	@field:SerializedName("rating")
	val rating: Double,

	@field:SerializedName("photos")
	val photos: List<PhotosItem>,

	@field:SerializedName("reference")
	val reference: String,

	@field:SerializedName("user_ratings_total")
	val userRatingsTotal: Int,

	@field:SerializedName("price_level")
	val priceLevel: Int,

	@field:SerializedName("scope")
	val scope: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("opening_hours")
	val openingHours: OpeningHours,

	@field:SerializedName("geometry")
	val geometry: Geometry,

	@field:SerializedName("vicinity")
	val vicinity: String,

	@field:SerializedName("plus_code")
	val plusCode: PlusCode,

	@PrimaryKey
	@field:SerializedName("place_id")
	val placeId: String,

	var liked: Boolean = false
) : Parcelable

@Parcelize
data class Viewport(

	@field:SerializedName("southwest")
	val southwest: Southwest,

	@field:SerializedName("northeast")
	val northeast: Northeast
) : Parcelable

@Parcelize
data class Location(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
) : Parcelable

@Parcelize
data class PhotosItem(

	@field:SerializedName("photo_reference")
	val photoReference: String,

	@field:SerializedName("width")
	val width: Int,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<String>,

	@field:SerializedName("height")
	val height: Int
) : Parcelable

@Parcelize
data class Southwest(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
) : Parcelable
