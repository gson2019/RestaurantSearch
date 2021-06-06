package com.alltrails.restaurantsearch.ui

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.alltrails.restaurantsearch.data.ResultsItem
import com.alltrails.restaurantsearch.data.remote.RestaurantRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class SearchResultsViewModel @ViewModelInject constructor(
    private val repository: RestaurantRepository,
    private val sharedPreferences: SharedPreferences,
    @Assisted state: SavedStateHandle) : ViewModel() {
    private var lastKnownLocation: Location? = null

    val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val searchResultsLiveData = repository.searchResultsLiveData

    val currentDestination = MutableLiveData<Int>()

    fun initiateRestaurantSearch(query: String) {
        currentQuery.value = query

        viewModelScope.launch {
            repository.getResults(query, getLastKnownLocation())
        }
    }

    private fun setLastKnownLocation(location: Location) {
        this.lastKnownLocation = location
        initiateRestaurantSearch(CURRENT_QUERY)
    }

    fun getLastKnownLocation(): LatLng {
        return LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
    }

    private fun isLocationGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    fun getDeviceLocation(context: Context) {
        if (isLocationGranted(context)) {
            try {
                val locationResult = LocationServices.getFusedLocationProviderClient(context).lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        task.result?.let {
                            setLastKnownLocation(it)
                        }
                    }
                }
            } catch (e: SecurityException) {
                Log.e("Exception: %s", e.message, e)
            }
        } else {
            ActivityCompat.requestPermissions(context as MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION_LIST
            )
        }
    }

    fun updateToSharedPreference(item: ResultsItem, checked: Boolean) {
        val editor = sharedPreferences.edit()
        if (checked) {
            editor.putString(item.placeId, item.name).apply()
        } else {
            editor.remove(item.placeId).apply()
        }
    }

    companion object {
        private const val CURRENT_QUERY = "chinese"
        const val DEFAULT_QUERY = "dogs"
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION_LIST = 1
    }
}