/*
package com.ravish.softplayer.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.PlayerScreen
import com.ravish.softplayer.data.service.PlayerService


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerMainScreen(
    playerService: PlayerService?, navigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("PlayerMainScreen:", "PlayerMainScreen")
    Log.d("playerService:", "$playerService")
    playerService?.let {
        MainScreen(it)
    }
}

@Composable
fun MainScreen(playerService: PlayerService) {
    with(playerService) {
        PlayerScreen(
            song = getCurrentSong().collectAsState(null).value,
            isPlaying = false,
            currentPositionMillis = getCurrentPosition()
                .collectAsState(null).value!!,
            totalDurationMillis = getDuration().collectAsState(null).value!!,
            isShuffleOn = false,
            repeatMode = RepeatMode.OFF,
            onPlayPauseClick = {
                if (getPlayer()?.isPlaying!!) {
                    getPlayer()!!.pause()
                } else {
                    getPlayer()!!.play()
                }
            },
            onNextClick = { getPlayer()?.next() },
            onPreviousClick = { getPlayer()?.previous() },
            onShuffleClick = {

            },
            onRepeatModeChange = {

            },
            onSeekBarPositionChange = { sliderPosition ->
                getPlayer()?.seekTo(
                    (sliderPosition * getPlayer()?.getPlayer()?.duration!!).toLong()
                )
            },
            modifier = Modifier.padding(10.dp)
        )
    }
}

*/
