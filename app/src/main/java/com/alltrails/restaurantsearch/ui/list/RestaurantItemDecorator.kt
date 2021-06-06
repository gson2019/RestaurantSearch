package com.alltrails.restaurantsearch.ui.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RestaurantItemDecorator(private val horizontalSpace: Int = 16,
private val verticalSpace: Int = 16) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = horizontalSpace
        outRect.left = horizontalSpace
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = verticalSpace
        }
        outRect.bottom = verticalSpace
    }
}