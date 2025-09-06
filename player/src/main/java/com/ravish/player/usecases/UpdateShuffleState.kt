package com.ravish.player.usecases

import com.ravish.player.MusicPlayer

class UpdateShuffleState(private val player: MusicPlayer?) {
    operator fun invoke(enable: Boolean) = player?.enableShuffle(enable)
}