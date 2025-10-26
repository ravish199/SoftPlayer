package com.ravish.softplayer.data.model.uistate

import com.ravish.player.data.model.SongItem
import kotlinx.coroutines.flow.StateFlow

data class PlayBackUIState(
    val isPlayingState:StateFlow<Boolean>,
    val playEndedState:StateFlow<Boolean>,
    val playingTrack: StateFlow<SongItem?>
)