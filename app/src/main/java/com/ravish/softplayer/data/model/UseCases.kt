package com.ravish.softplayer.data.model

import com.ravish.player.usecases.Pause
import com.ravish.player.usecases.Play
import com.ravish.player.usecases.PlayNext
import com.ravish.player.usecases.PlayPrevious
import com.ravish.player.usecases.PlaySelectedSong
import com.ravish.player.usecases.SeekTo
import com.ravish.player.usecases.SetRepeatMode
import com.ravish.player.usecases.Stop
import com.ravish.player.usecases.UpdateShuffleState
import com.ravish.softplayer.data.PlayerControlState

data class UseCases(
    val playSelected: PlaySelectedSong,
    val play: Play,
    val playNext: PlayNext,
    val playPrevious: PlayPrevious,
    val pause: Pause,
    val seekTo: SeekTo,
    val stop: Stop,
    val shuffle: UpdateShuffleState,
    val setRepeatMode: SetRepeatMode
)