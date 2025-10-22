package com.ravish.player.usecases

import com.ravish.player.MusicPlayer

class Pause(private val player: MusicPlayer?) {
    operator fun invoke() {
        player?.pause()
    }
}