package com.nymbleup.inventory.views

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(context: Context): DividerItemDecoration(context, context.resources.configuration.orientation) {

    private val space = 300

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)

        if (state.itemCount == 1) {
            return
        }

        val isLast = position == state.itemCount - 1
        if (isLast) {
            outRect.bottom = space
            outRect.top = 0 //don't forget about recycling...
        }else {
            outRect.bottom = 0
            outRect.top = 0
        }
        /* if(position == 0){
            outRect.top = space;
            // don't recycle bottom if first item is also last
            // should keep bottom padding set above
            if(!isLast)
                outRect.bottom = 0;
        }*/
    }

}