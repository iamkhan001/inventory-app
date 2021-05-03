package com.nymbleup.inventory.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel


class ShareUtils(private val context: Context) {

    private val tag = ShareUtils::class.java.simpleName

    companion object {

        @JvmStatic
        fun checkAppInstalled(context: Context, app: String): Boolean {

            val appPackage = if (app == "WhatsApp") {
                "com.whatsapp"
            } else {
                ""
            }

            if (appPackage.isEmpty()) {
                return false
            }

            val pm = context.packageManager
            try {
                pm.getPackageInfo(appPackage, PackageManager.GET_ACTIVITIES)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
            }

            return false
        }

        fun shareFile(context: Context, path: String) {

            val file = File(path)
            val uri = Uri.fromFile(file)

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                flags = FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }


        fun shareFile(context: Context, fileUri: Uri) {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/*"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                flags = FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    fun shareAsText(text: String) {

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)

    }



    fun shareAsText(text: String, app: String) {

        Log.e(tag, "share option: $app")
        Log.e(tag, "content text: $text")

        when (app) {

            "WhatsApp" -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.setPackage("com.whatsapp")
                intent.putExtra(Intent.EXTRA_TEXT, text)
                try {
                    context.startActivity(intent)
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(context, "WhatsApp is not installed!", Toast.LENGTH_SHORT).show()
                }

            }
            "Email" -> {

                Log.e(tag, "email ")

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "message/rfc822"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)

            }
            else -> {

                val smsApp = Telephony.Sms.getDefaultSmsPackage(context)

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.setPackage(smsApp)
                intent.putExtra(Intent.EXTRA_TEXT, text)
                try {
                    context.startActivity(intent)
                } catch (ex: android.content.ActivityNotFoundException) {

                    ex.printStackTrace()

                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)

                }

            }

        }


    }

    fun shareFile(uri: Uri, app: String) {

        Log.e(tag, "share option: $app")

        when (app) {

            "WhatsApp" -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.setPackage("com.whatsapp")
                intent.type = "application/pdf"
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                try {
                    context.startActivity(intent)
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(context, "WhatsApp is not installed!", Toast.LENGTH_SHORT).show()
                }

            }
            "Email" -> {

                Log.e(tag, "email ")

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)

            }

        }

    }

    @Throws(IOException::class)
    fun copyFile(file: File, name: String) {

        Log.e(tag, "copyFile $name ${file.absolutePath} ")

        val folder = File("/storage/emulated/0/" + Environment.DIRECTORY_DOCUMENTS, "TimePay")

        if (!folder.exists()) {
            folder.mkdirs()
        }

        val newFile = File(folder, name)

        if (!newFile.exists()) {
            newFile.mkdirs()
        }

        var outputChannel: FileChannel? = null
        var inputChannel: FileChannel? = null
        try {
            outputChannel = FileOutputStream(newFile).channel
            inputChannel = FileInputStream(file).channel
            inputChannel!!.transferTo(0, inputChannel.size(), outputChannel)
            inputChannel.close()
            //file.delete()

            Toast.makeText(context, "File Saved to Documents / TimePay / $name", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(context, "Unable to save file", Toast.LENGTH_SHORT).show()

        } finally {
            inputChannel?.close()
            outputChannel?.close()
        }

    }


    fun copyText(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("share", text)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(context, "Text copied to clipboard!", Toast.LENGTH_SHORT).show()
    }


}