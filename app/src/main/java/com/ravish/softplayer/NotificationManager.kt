/*
package com.ravish.softplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

object PlayerNotificationManager {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getNotification(context: Context):Notification {
        val mChannel = NotificationChannel("Music", "Music Player", NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
       return NotificationCompat.Builder(context, mChannel.id).build()
    }

}*/
