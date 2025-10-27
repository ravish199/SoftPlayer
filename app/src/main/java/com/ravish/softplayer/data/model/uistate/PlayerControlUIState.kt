package com.ravish.softplayer.data.model.uistate

import com.ravish.softplayer.data.model.RepeatMode
import kotlinx.coroutines.flow.StateFlow

data class PlayerControlUIState(
    val playPauseState:StateFlow<Boolean>,
    val shuffleState:StateFlow<Boolean>,
    val repeatModeState:StateFlow<RepeatMode>
)