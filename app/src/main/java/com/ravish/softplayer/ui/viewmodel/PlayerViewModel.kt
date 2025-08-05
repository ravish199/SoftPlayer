package com.ravish.softplayer.ui.viewmodel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.ravish.softplayer.SongItem
import com.ravish.softplayer.data.PlayerControlUIState
import com.ravish.softplayer.data.service.PlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class PlayerViewModel @Inject constructor(): ViewModel() {

    @SuppressLint("StaticFieldLeak")
    private var playerService: PlayerService? = null
    private val _songLoadProgressUiState = MutableStateFlow(Pair(0, 0))
    val songLoadProgressUiState: StateFlow<Pair<Int, Int>> = _songLoadProgressUiState
    private var _audioList = MutableStateFlow(emptyList<SongItem>())
    val audioList: StateFlow<List<SongItem>> = _audioList

    private val _backgroundState = MutableStateFlow<Bitmap?>(null)
    val backgroundState: StateFlow<Bitmap?> = _backgroundState

    private val _playerControlUiState = MutableStateFlow(PlayerControlUIState(null))

    init {

    }

    fun updateBackground(bitmap: Bitmap?) {
        _backgroundState.value = bitmap
    }

    fun initService(playerService: PlayerService?) {
        this.playerService = playerService
    }

    fun loadAudioFiles(onLoaded: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            playerService?.loadAudioFiles({
                _audioList.value = it
                Log.d("PlayerViewModel:", "songlist:${audioList.value.size}")
                Log.d("PlayerViewModel:", "songlist.hashcode:${audioList.hashCode()}")
                kotlinx.coroutines.delay(2000L)
                onLoaded.invoke()
            }) { count, total ->
                _songLoadProgressUiState.value = Pair(count, total)
            }
            /*      playerService?.loadAudioFiles(onLoaded) { count, total ->
                      _songLoadProgressUiState.value = Pair(count, total)
                      if (count == total) {
                          Log.d("PlayerViewModel:", "loadAudioFiles: count:${count}, total:${total}")
                          playerService?.getSongList()?.let {
                              audioList = it
                              Log.d("PlayerViewModel:", "songlist:${audioList.size}")
                          }
                          kotlinx.coroutines.delay(1000L)
                              onLoaded.invoke()
                      }
                  }*/
        }
    }

/*    fun getCurrentPosition(): StateFlow<Long>? = playerService?.musicPlayer?.currentPositionUpdater

    fun getTotalDuration(): StateFlow<Long>? = playerService?.musicPlayer?.totalDurationUpdater

    fun getPlayerState(): StateFlow<Boolean>? = playerService?.musicPlayer?.isPlayingUpdater
    fun getPlayingUpdater(): StateFlow<Boolean>? = playerService?.musicPlayer?.isPlayingUpdater
    fun getSongUpdater(): StateFlow<MediaItem?>? = playerService?.musicPlayer?.songUpdater*/
}