package com.alltrails.restaurantsearch.data.remote

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.alltrails.restaurantsearch.data.ApiResult
import com.alltrails.restaurantsearch.data.Error
import com.alltrails.restaurantsearch.data.ResultsItem
import com.alltrails.restaurantsearch.data.Success
import com.alltrails.restaurantsearch.network.GooglePlaceApi
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import javax.inject.Singleton

const val NO_PLACE_ID_FOUND = "No Place Id Found"

@Singleton
class RestaurantRepository @Inject constructor(
    private val placeApi: GooglePlaceApi,
    private val sharedPreferences: SharedPreferences
) {

    val searchResultsLiveData: MutableLiveData<ApiResult> by lazy { MutableLiveData() }

    suspend fun getResults(query: String, latLng: LatLng) {
        val token = getPageToken(query)

        try {
            val response = if (token.second == null) {
                placeApi.searchNearbyRestaurants(
                    location = "${latLng.latitude},${latLng.longitude}",
                    radius = 10000,
                    type = "restaurant",
                    keyword = query,
                )
            } else {
                placeApi.searchNearbyRestaurants(
                    location = "${latLng.latitude},${latLng.longitude}",
                    radius = 10000,
                    pageToken = token.second!!
                )
            }

            setPageToken(query, response.nextPageToken)

            searchResultsLiveData.postValue(Success(postProcessData(response.results)))
        } catch (e: Exception) {
            searchResultsLiveData.postValue(Error(e.localizedMessage))
        }
    }

    private fun postProcessData(restaurantItems: List<ResultsItem>) : List<ResultsItem> {
        return restaurantItems.map { restaurantItem ->
            val returned = sharedPreferences.getString(restaurantItem.placeId, NO_PLACE_ID_FOUND)
            restaurantItem.liked = returned != NO_PLACE_ID_FOUND
            restaurantItem
        }
    }

    private fun getPageToken(query: String): Pair<String?, String?> {
        val prevQuery = sharedPreferences.getString(PREV_QUERY, null)

        val nextPageToken = if (prevQuery == query) {
            sharedPreferences.getString(NEXT_PAGE_TOKEN, null)
        } else {
            null
        }

        return Pair(prevQuery, nextPageToken)
    }

    private fun setPageToken(query: String, pageToken: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(PREV_QUERY, query)
        editor.putString(NEXT_PAGE_TOKEN, pageToken)
        editor.apply()
    }

    companion object {
        const val PREV_QUERY = "PREV_QUERY"
        const val NEXT_PAGE_TOKEN = "NEXT_PAGE_TOKEN"
    }
}