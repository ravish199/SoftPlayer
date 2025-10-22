package com.ravish.softplayer.data.model

import kotlinx.coroutines.flow.StateFlow

data class PlayBackUIState(
    var isPlayingState:StateFlow<Boolean>,
    var playEndedState:StateFlow<Boolean>
)