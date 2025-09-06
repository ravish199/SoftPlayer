package com.ravish.player.usecases

import com.ravish.player.MusicPlayer

class PlayPrevious(private val player: MusicPlayer?) {
    operator fun invoke() = player?.previous()
}