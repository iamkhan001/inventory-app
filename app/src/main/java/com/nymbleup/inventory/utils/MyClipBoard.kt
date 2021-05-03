package com.nymbleup.inventory.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object MyClipBoard {

    fun copyToClip(context: Context, text: String?, title: String) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(title, text)
        clipboard?.setPrimaryClip(clip)
        MyMessage.showToast(context, "$title copied!")
    }

}