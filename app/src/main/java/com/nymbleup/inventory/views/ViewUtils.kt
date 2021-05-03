package com.nymbleup.inventory.views

import android.graphics.Paint
import android.graphics.Typeface
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView


object ViewUtils {

    fun addDivider(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    fun underlineText(textView: TextView) {
        textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        textView.setTypeface(null, Typeface.BOLD)
    }

    fun removeUnderlineFromText(textView: TextView) {
        textView.paintFlags = 0
        textView.setTypeface(null, Typeface.NORMAL)
    }

}