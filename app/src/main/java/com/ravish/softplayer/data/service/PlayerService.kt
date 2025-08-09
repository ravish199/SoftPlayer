package com.ravish.softplayer.data.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ServiceCompat
import com.ravish.player.MediaFileManager
import com.ravish.player.MyMusicPlayer
import com.ravish.softplayer.PlayerNotificationManager

import com.ravish.softplayer.data.PlayerControlState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PlayerService : Service() {

    var musicPlayer: MyMusicPlayer? = null
    private var mediaFileManager: MediaFileManager? = null
    val _playerState = MutableStateFlow<PlayerControlState?>(null)

    override fun onCreate() {
        super.onCreate()
        Log.d("PlayerService:", "onCreate")
        CoroutineScope(Dispatchers.IO).launch {
            musicPlayer = MyMusicPlayer(applicationContext)
            musicPlayer?.initializePlayer()
        }

        mediaFileManager = MediaFileManager(this)
    }

    suspend fun loadAudioFiles(onLoaded:suspend (List<com.ravish.player.data.model.SongItem>) -> Unit, loadProgress: suspend (Int, Int) -> Unit) {
        Log.d("PlayerService:", "loadAudioFiles")
            mediaFileManager?.loadAudioFiles(onLoaded, loadProgress)

    }

    fun getSongList() = mediaFileManager?.audioList

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("PlayerService:", "onStartCommand")
        ServiceCompat.startForeground(this, 1, PlayerNotificationManager.getNotification(this),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        return START_STICKY

    }


     fun play() {
        musicPlayer?.play()
    }

    suspend fun playSingleSong(songUri: Uri) {
        musicPlayer?.playSingleSong(songUri)
    }

    fun getTotalDuration() = musicPlayer?.totalDurationUpdater
    fun currentPosition() = musicPlayer?.currentPositionUpdater

    suspend fun pause() {
        musicPlayer?.pause()
    }

     fun stop() {
        musicPlayer?.stop()
    }

    suspend fun next() {
        musicPlayer?.next()
    }

    suspend fun previous() {
        musicPlayer?.previous()
    }

     fun seekTo(positionMs: Long) {
        musicPlayer?.seekTo(positionMs)
    }

    fun getPlayEndedState() = musicPlayer?.onPlayEndedUpdater


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
       // musicPlayer?.releasePlayer()
       // musicPlayer = null
        Log.d("PlayerService:", "onDestroy")
    }


}