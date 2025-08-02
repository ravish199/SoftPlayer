package com.ravish.softplayer.data

import kotlinx.coroutines.flow.Flow

data class PlayerControlUIState(
    val playPauseState:Flow<PlayerControlState>?
)