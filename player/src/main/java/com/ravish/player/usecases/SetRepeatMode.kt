package com.ravish.player.usecases

import androidx.media3.common.Player
import com.ravish.player.MusicPlayer

class SetRepeatMode(private val player: MusicPlayer?) {
    operator fun invoke(repeatMode: Int) = player?.setRepeatMode(repeatMode)
}