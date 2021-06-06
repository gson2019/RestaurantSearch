package com.alltrails.restaurantsearch.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alltrails.restaurantsearch.R
import com.alltrails.restaurantsearch.data.ApiResult
import com.alltrails.restaurantsearch.data.ResultsItem
import com.alltrails.restaurantsearch.ui.SearchResultsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_restaurant_list.*

@AndroidEntryPoint
class RestaurantListFragment : Fragment(), RestaurantListAdapter.OnItemClickListener,
View.OnClickListener {
    private lateinit var adapter: RestaurantListAdapter
    private val viewModel by viewModels<SearchResultsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // observer initialization
        initObserver()

        // recycler view
        initRestaurantList()

        // click listener
        initClick()
    }

    private fun initClick() {
        button_retry.setOnClickListener(this)
    }

    override fun onItemClick(item: ResultsItem, checked: Boolean) {
        viewModel.updateToSharedPreference(item, checked)
    }

    override fun onClick(v: View?) {
        viewModel.currentQuery.value?.let { viewModel.initiateRestaurantSearch(it) }
    }

    private fun initObserver() {
        viewModel.searchResultsLiveData.observe(viewLifecycleOwner, {
            progress_bar.isVisible = it is ApiResult.Loading
            when(it){
                is ApiResult.Success -> {
                    progress_bar.isVisible = false
                    adapter.updateDataSource(it.data as MutableList<ResultsItem>)
                }
                is ApiResult.Error -> {
                    progress_bar.isVisible = false
                    button_retry.isVisible = true
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initRestaurantList() {
        adapter = RestaurantListAdapter(requireContext() ,this)

        recycler_view.setHasFixedSize(true)
        recycler_view.addItemDecoration(
            RestaurantItemDecorator()
        )
        recycler_view.adapter =  adapter
    }
}