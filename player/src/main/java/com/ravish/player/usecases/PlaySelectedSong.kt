package com.ravish.player.usecases

import com.ravish.player.MusicPlayer

class PlaySelectedSong(private val player: MusicPlayer?) {
    operator fun invoke(index: Int) = player?.play(index)
}