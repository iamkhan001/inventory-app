package com.nymbleup.inventory.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

    fun show(context: Context) {
        val imm: InputMethodManager? = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun hide(context: Context) {
        val imm: InputMethodManager? = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

}