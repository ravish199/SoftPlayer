package com.ravish.player.usecases

import com.ravish.player.MusicPlayer

class Play(private val player: MusicPlayer?) {
    operator fun invoke() = player?.play()
}