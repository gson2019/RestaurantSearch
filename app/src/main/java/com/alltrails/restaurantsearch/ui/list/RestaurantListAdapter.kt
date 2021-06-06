package com.alltrails.restaurantsearch.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alltrails.restaurantsearch.R
import com.alltrails.restaurantsearch.R.layout.item_restaurant
import com.alltrails.restaurantsearch.data.ResultsItem
import com.alltrails.restaurantsearch.databinding.ItemRestaurantBinding
import com.alltrails.restaurantsearch.getPriceLevelString
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class RestaurantListAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RestaurantListAdapter.DataViewHolder>() {
    private val dataList: MutableList<ResultsItem> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(item_restaurant, parent, false)
        return DataViewHolder(view)
    }

    private fun getItem(position: Int): ResultsItem {
        return dataList[position]
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentRestaurant = getItem(position)

        currentRestaurant.let {
            with(holder.binding) {
                Glide.with(imageView)
                    .load(it.icon)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)
                ratingBar.rating = it.rating.toFloat()
                textViewUserName.text = it.name
                reviewCount.text = "(${it.userRatingsTotal})"
                statusTv.text = context.getString(R.string.restaurant_status, it.priceLevel.getPriceLevelString(), it.businessStatus)
                toggleLike.isChecked = it.liked
                toggleLike.setOnCheckedChangeListener { _, checked ->
                    listener.onItemClick(currentRestaurant, checked)
                }
            }
        }
    }

    fun updateDataSource(list: MutableList<ResultsItem>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(item: ResultsItem, checked: Boolean)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemRestaurantBinding by lazy { ItemRestaurantBinding.bind(itemView) }
    }
}