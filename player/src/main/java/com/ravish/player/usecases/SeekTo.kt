package com.ravish.player.usecases

import com.ravish.player.MusicPlayer

class SeekTo(private val player: MusicPlayer?) {
    operator fun invoke(position: Long) = player?.seekTo(position)
}