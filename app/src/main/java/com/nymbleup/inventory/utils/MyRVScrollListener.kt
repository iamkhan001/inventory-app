package com.nymbleup.inventory.utils

import androidx.recyclerview.widget.RecyclerView

abstract class MyRVScrollListener : RecyclerView.OnScrollListener() {

    var scrollDist = 0
    var isVisible = true

    companion object {
        private const val MINIMUM = 50
    }
    
    abstract fun hide()

    abstract fun show()
    
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (isVisible && scrollDist > MINIMUM) {
            hide()
            scrollDist = 0
            isVisible = false
        }
        else if (!isVisible && scrollDist < -MINIMUM) {
            show()
            scrollDist = 0
            isVisible = true
        }

        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy
        }

    }

}