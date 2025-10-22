package com.ravish.softplayer.data.model

import kotlinx.coroutines.flow.StateFlow

data class PlayerControlUIState(
    val playPauseState:StateFlow<Boolean>,
    val shuffleState:StateFlow<Boolean>,
    val repeatModeState:StateFlow<RepeatMode>
)