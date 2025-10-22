package com.ravish.player.usecases

import com.ravish.player.MusicPlayer

class PlayNext(private val player: MusicPlayer?) {
    operator fun invoke() = player?.next()
}