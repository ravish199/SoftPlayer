package com.ravish.player

import androidx.media3.common.Player

interface PlaybackActionDataSource {
    fun play()
    fun play(index: Int)
    fun pause()
    fun stop()
    fun next()
    fun previous()
    fun seekTo(positionMs: Long)
    fun enableShuffle(enable: Boolean)
    fun setRepeatMode(repeatMode: Int)
}