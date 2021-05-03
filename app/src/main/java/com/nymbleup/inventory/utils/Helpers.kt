package com.nymbleup.inventory.utils

import android.content.Context
import java.util.regex.Matcher
import java.util.regex.Pattern

object Helpers {


    //email validation
    fun validateEmailAddress(emailAddress: String): Boolean {
        val expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val inputStr: CharSequence = emailAddress
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(inputStr)
        return matcher.matches()
    }

    fun isOnlyNumbers(value: String): Boolean {
        return try {
            value.toLong()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun loadJSONFromAsset(context: Context, file: String): String {
        return context.assets.open(file).bufferedReader().use { reader ->
            reader.readText()
        }
    }

    fun upperCaseFirst(name: String?): String {
        if (name.isNullOrEmpty()) {
            return ""
        }

        val p1 = name[0].toUpperCase()
        val p2 = name.substring(1)
        return "$p1$p2"
    }

}