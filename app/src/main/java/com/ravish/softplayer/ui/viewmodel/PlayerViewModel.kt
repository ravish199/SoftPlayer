package com.ravish.softplayer.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.ravish.softplayer.SongItem
import com.ravish.softplayer.data.PlayerControlUIState
import com.ravish.softplayer.data.service.PlayerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Singleton

@Singleton
class PlayerViewModel:   ViewModel() {

    private var playerService: PlayerService? = null

   private val _playerControlUiState = MutableStateFlow(PlayerControlUIState(null))
    init {

    }

    fun initService(playerService: PlayerService) {
        this.playerService = playerService
    }

    fun getCurrentPosition(): StateFlow<Long>? = playerService?.musicPlayer?.currentPositionUpdater

    fun getTotalDuration(): StateFlow<Long>? = playerService?.musicPlayer?.totalDurationUpdater

    fun getPlayerState(): StateFlow<Boolean>? = playerService?.musicPlayer?.isPlayingUpdater
    fun getPlayingUpdater():StateFlow<Boolean>? = playerService?.musicPlayer?.isPlayingUpdater
    fun getSongUpdater(): StateFlow<MediaItem?>? = playerService?.musicPlayer?.songUpdater
}