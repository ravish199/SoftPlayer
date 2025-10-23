package com.ravish.softplayer.data.service


import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager

import com.ravish.player.MediaFileManager
import com.ravish.player.MusicPlayer

import com.ravish.softplayer.data.PlayerControlState
import com.ravish.softplayer.ui.notification.MediaNotificationManager

import com.ravish.soundeffects.AudioEffectManager
import com.ravish.soundeffects.EqualizerEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@UnstableApi
class PlayerService : androidx.lifecycle.LifecycleService() {

    var musicPlayer: MusicPlayer? = null
    var audioEffectManager: AudioEffectManager? = null
    var mediaFileManager: MediaFileManager? = null
    val _playerState = MutableStateFlow<PlayerControlState?>(null)
    private var notificationManager: MediaNotificationManager? = null

    private val notificationListener = object : PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            Log.d("MediaNotificationManager:", "onNotificationPosted")
            if (ongoing) {
                Log.d("MediaNotificationManager:", "onNotificationPosted ongoing")
                // If the notification is ongoing, make sure the service is in the foreground
                startForeground(notificationId, notification)
            } else {
                Log.d("MediaNotificationManager:", "stopForeground")
                // If it's not ongoing, the service can be stopped
              //  stopForeground(STOP_FOREGROUND_REMOVE)
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("PlayerService:", "onCreate")
            musicPlayer = MusicPlayer(applicationContext)
            audioEffectManager = EqualizerEffect()
            musicPlayer?.initializePlayer()

                musicPlayer?.getPlayer()?.let {
                    notificationManager = MediaNotificationManager(
                        this@PlayerService,
                        it,
                        notificationListener
                    )
                }
                mediaFileManager = MediaFileManager(this@PlayerService)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("PlayerService:", "onStartCommand")
       /* ServiceCompat.startForeground(
            this, 1, MediaNotificationManager.getNotification(this),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )*/
        notificationManager?.showNotification()
        return START_STICKY

    }

    inner class ServiceBinder : Binder() {
        fun getService(): PlayerService {
            return this@PlayerService
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return ServiceBinder()
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager?.hideNotification()
        musicPlayer?.releasePlayer()
        musicPlayer = null
        Log.d("PlayerService:", "onDestroy")
    }


}