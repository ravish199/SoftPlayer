package com.ravish.softplayer.data

sealed class RepeatMode {
    data object RepeatOne: RepeatMode()
    data object RepeatALl:RepeatMode()
    data object RepeatNone:RepeatMode()
}