package com.alltrails.restaurantsearch.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.alltrails.restaurantsearch.R
import com.alltrails.restaurantsearch.data.ResultsItem
import com.alltrails.restaurantsearch.data.Success
import com.alltrails.restaurantsearch.ui.SearchResultsViewModel.Companion.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION_LIST
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_restaurant_list.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private val resultsViewModel: SearchResultsViewModel by viewModels()

    private var searchDisposal: CompositeDisposable? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.checkoutNavContainerView) as NavHostFragment

        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        resultsViewModel.getDeviceLocation(this)
        supportActionBar?.hide()

        setListeners()

        setObservers()
    }

    private fun setObservers() {
        resultsViewModel.currentDestination.observe(
            this,
            { destination ->
                val bundle = Bundle()
                bundle.putParcelableArrayList(
                    "EXTRA_DATA",
                    (resultsViewModel.searchResultsLiveData.value as Success).data as ArrayList<ResultsItem>
                )
                val buttonDrawable = if (destination == R.id.listFragment) {
                    R.drawable.ic_location_map
                } else {
                    R.drawable.ic_location_list
                }
                toggleBtn.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        this,
                        buttonDrawable
                    ), null, null, null
                )
                navController.navigate(destination, bundle)
            }
        )

        RxRestaurantSearch.fromSearchView(searchView = searchView)
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter { item ->
                item.isNotEmpty()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                progress_bar?.let { it.isVisible = true }
                resultsViewModel.initiateRestaurantSearch(query)
                searchView.clearFocus()
            }
    }

    private fun setListeners() {
        toggleBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                resultsViewModel.currentDestination.value = R.id.mapFragment
            } else {
                resultsViewModel.currentDestination.value = R.id.listFragment
            }
        }

        searchView.setOnSearchClickListener {
            it.requestFocus()
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    // [START maps_current_place_on_request_permissions_result]
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION_LIST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    resultsViewModel.getDeviceLocation(this)
                }
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}