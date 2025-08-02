package com.ravish.softplayer.data.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.ravish.softplayer.MediaFileManager
import com.ravish.softplayer.MyMusicPlayer
import com.ravish.softplayer.data.PlayerControlState
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerService : Service() {

    var musicPlayer: MyMusicPlayer? = null
    private var mediaFileManager: MediaFileManager? = null
    val _playerState = MutableStateFlow<PlayerControlState?>(null)

    override fun onCreate() {
        super.onCreate()
        Log.d("PlayerService:", "onCreate")
        musicPlayer = MyMusicPlayer(applicationContext)
        musicPlayer?.initializePlayer()
        mediaFileManager = MediaFileManager(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("PlayerService:", "onStartCommand")
        return START_STICKY

    }


    fun play() {
        musicPlayer?.play()
    }

    fun pause() {
        musicPlayer?.pause()
    }

    fun stop() {
        musicPlayer?.stop()
    }

    fun next() {
        musicPlayer?.next()
    }

    fun previous() {
        musicPlayer?.previous()
    }

    fun seekTo(positionMs: Long) {
        musicPlayer?.seekTo(positionMs)
    }


    inner class ServiceBinder : Binder() {
        fun getService(): PlayerService {
            return this@PlayerService
        }
    }

    override fun onBind(p0: Intent?): IBinder {
        return ServiceBinder()
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer?.releasePlayer()
        musicPlayer = null
        Log.d("PlayerService:", "onDestroy")
    }


}