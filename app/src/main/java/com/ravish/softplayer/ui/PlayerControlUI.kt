package com.ravish.softplayer.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ravish.softplayer.R
import com.ravish.player.data.model.SongItem
import com.ravish.softplayer.data.model.RepeatMode
import com.ravish.softplayer.ui.theme.ButtonBackgroundColor
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

val repeatArray = arrayOf(
    Pair(RepeatMode.REPEAT_NONE, R.drawable.icon_repeat_none),
    Pair(RepeatMode.REPEAT_ALL, R.drawable.icon_repeat_all),
    Pair(RepeatMode.REPEAT_ONE, R.drawable.icon_repeat_one)
)
var index = 0
var songState = mutableStateOf<SongItem?>(null)
var _isPlayState = mutableStateOf(false)
var isPlayState = _isPlayState
private var playerViewModel:PlayerViewModel? = null

@Composable
fun DrawPlayerControl(viewModel: PlayerViewModel, modifier: Modifier) {
    playerViewModel = viewModel
    var shuffleState by remember { mutableStateOf(false) }
    var repeatState by remember { mutableStateOf(Pair(RepeatMode.REPEAT_ONE, R.drawable.icon_repeat_one)) }
    val playState by isPlayState
    Row(
        modifier = modifier.background(ButtonBackgroundColor),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AddIcon(modifier = Modifier
            .wrapContentSize()
            .clickable {
                shuffleState = !shuffleState
            }, icon = R.drawable.icon_shuffel, selected = shuffleState)
        AddIcon(modifier = Modifier
            .wrapContentSize()
            .clickable {

            }, icon = R.drawable.icon_previous, selected = true)
        AddIcon(modifier = Modifier
            .wrapContentSize()
            .clickable {
                isPlayState.value = !(isPlayState.value)
                Log.d("Click:", "Click:${isPlayState.value}")
            }, icon = if(playState) R.drawable.icon_pause else R.drawable.icon_play_cirlce, selected = true)
        AddIcon(modifier = Modifier
            .wrapContentSize()
            .clickable {
            }, icon = R.drawable.icon_next, selected = true)
        AddIcon(modifier = Modifier
            .wrapContentSize()
            .clickable {
                repeatState = getRepeatState()
            }, icon = repeatState.second, selected = (repeatState.first != RepeatMode.REPEAT_NONE))
    }

    if(isPlayState.value) {
        play()
    }else {
        pause()
    }
}

private fun play() {
    songState.value?.let {
        playerViewModel?.songSeekValue?.let { seek ->
            if(seek > 0) {
                Log.d("PlayerViewModel:", "seek:${seek}")
                playerViewModel?.play()
                playerViewModel?.seekTo((seek * 1000L).toLong())
            } else {

                playerViewModel?.playSingleSong(it.contentUri)
            }
            _isPlayState.value = true
        }
    }
}

private fun pause() {
        playerViewModel?.pause()
        _isPlayState.value = false
}

private fun stop() {
    playerViewModel?.stop()
    _isPlayState.value = false
}

fun loadPlayerSong(songItem: SongItem?) {
    pause()
    stop()
    songState.value = songItem
    play()
}

fun getRepeatState(): Pair<RepeatMode, Int> {
    index = (++index) % repeatArray.size
    return repeatArray[index]
}

@Preview
@Composable
fun DrawPlayerControlPreview() {
    DrawPlayerControl(
        viewModel = viewModel(),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
    )
}
