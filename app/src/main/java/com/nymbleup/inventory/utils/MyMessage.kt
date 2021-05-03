package com.nymbleup.inventory.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.mirobotic.dialog.myDialog.SweetAlertDialog

object MyMessage {

    fun showToast(context: Context?, msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showBar(view: View?, msg: String) {
        view?.let {
            Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    fun showBar(activity: Activity?, msg: String) {
        if (activity == null) {
            return
        }
        Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show()
    }

    fun showSuccess(context: Context?, title: String, message: String) {
        if (context == null) {
            return
        }

        val alertDialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
        alertDialog.titleText = title
        alertDialog.contentText = message
        alertDialog.confirmText = "Ok"
        alertDialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        alertDialog.show()
    }

    fun showFailed(context: Context?, message: String) {
        if (context == null) {
            return
        }

        val alertDialog = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        alertDialog.titleText = "Failed!"
        alertDialog.contentText = message
        alertDialog.confirmText = "Ok"
        alertDialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        alertDialog.show()
    }

    fun getProgressAlert(context: Context, title: String): SweetAlertDialog {

        val alertDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        alertDialog.setCancelable(false)
        alertDialog.titleText = title
        return alertDialog
    }

}