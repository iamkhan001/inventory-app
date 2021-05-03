package com.nymbleup.inventory.utils

object NameInitialsUtils {

    fun getInitials(name: String?): String {

        if(name.isNullOrEmpty()) {
            return ""
        }
        val newName = name.trim()
        return if (newName.contains(" ")) {
            val char1 = newName[0].toUpperCase()
            val char2 = newName[newName.indexOf(" ")+1].toUpperCase()
            "$char1$char2"
        }else {
         "${newName[0].toUpperCase()}"
        }
    }
}