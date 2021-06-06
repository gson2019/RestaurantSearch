package com.alltrails.restaurantsearch.ui.map

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.alltrails.restaurantsearch.R
import com.alltrails.restaurantsearch.data.Error
import com.alltrails.restaurantsearch.data.ResultsItem
import com.alltrails.restaurantsearch.data.Success
import com.alltrails.restaurantsearch.ui.SearchResultsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class RestaurantMapFragment: SupportMapFragment(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private var lastClicked: Marker? = null
    private val resultsViewModel by activityViewModels<SearchResultsViewModel>()
    var restaurantList: List<ResultsItem>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mapFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        getMapAsync(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultsViewModel.searchResultsLiveData.observe(viewLifecycleOwner, {
            when(it){
                is Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                is Success -> {
                    restaurantList = it.data as List<ResultsItem>
                    updateMap()
                }
            }
        })
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        updateMap()
    }

    private fun updateMap() {
        if (map != null && restaurantList.isNullOrEmpty().not()) {
            val restaurantWindowAdapter = RestaurantInfoWindowAdapter(requireActivity())
            map!!.clear()
            map!!.setInfoWindowAdapter(restaurantWindowAdapter)
            map!!.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    resultsViewModel.getLastKnownLocation(),
                    16.toFloat()
                )
            )
            val boundsBuilder = LatLngBounds.builder()
            restaurantList!!.forEach { restaurantItem ->
                val markerOptions = MarkerOptions()
                    .position(
                        LatLng(
                            restaurantItem.geometry.location.lat,
                            restaurantItem.geometry.location.lng
                        )
                    )
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_unselected))
                val marker = map?.addMarker(markerOptions)
                marker?.position?.let { boundsBuilder.include(it) }
                marker?.tag = restaurantItem
            }
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 0)

            map?.animateCamera(
                cameraUpdate
            )

            map?.setOnMarkerClickListener { p0 ->
                lastClicked?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_unselected))
                p0.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_selected))
                p0.showInfoWindow()
                lastClicked = p0
                map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        p0.position,
                        16.toFloat()
                    )
                )
                true
            }

            map?.setOnMapClickListener {
                if (null != lastClicked) {
                    lastClicked!!.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_unselected));
                }
                lastClicked = null
            }
        }
    }
}