package com.example.unfold.util

import android.app.DownloadManager
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.net.toUri

class Downloader(context: Context) {

    private val downloadManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(DownloadManager::class.java)
    } else {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    fun download(url: String, name: String): Long {
        val filename = "$name.png"
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/png")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(filename)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        return downloadManager.enqueue(request)
    }
}
