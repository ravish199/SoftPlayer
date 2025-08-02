package com.ravish.softplayer.data

sealed class PlayerControlState {
    data object Next : PlayerControlState()
    data object Previous : PlayerControlState()
    data object Shuffle : PlayerControlState()
    data class Repeat(val value: RepeatMode) : PlayerControlState()
    data object Play : PlayerControlState()
    data object Pause : PlayerControlState()
}
