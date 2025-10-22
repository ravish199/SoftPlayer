package com.ravish.player.usecases

import com.ravish.player.MusicPlayer

class Stop(private val player: MusicPlayer?) {
    operator fun invoke() = player?.stop()
}