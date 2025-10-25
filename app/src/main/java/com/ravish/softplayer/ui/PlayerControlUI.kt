package com.ravish.softplayer.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ravish.softplayer.R
import com.ravish.softplayer.data.model.RepeatMode
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

val repeatArray = arrayOf(
    Pair(RepeatMode.REPEAT_NONE, R.drawable.icon_repeat_none),
    Pair(RepeatMode.REPEAT_ALL, R.drawable.icon_repeat_all),
    Pair(RepeatMode.REPEAT_ONE, R.drawable.icon_repeat_one)
)
var index = 0
private var playerViewModel: PlayerViewModel? = null

@Composable
fun DrawPlayerControl(viewModel: PlayerViewModel, modifier: Modifier) {
    playerViewModel = viewModel
    var shuffleState by remember { mutableStateOf(false) }
    var repeatState by remember {
        mutableStateOf(
            Pair(
                RepeatMode.REPEAT_ONE,
                R.drawable.icon_repeat_one
            )
        )
    }


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AddIcon(
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    shuffleState = !shuffleState
                    viewModel.shuffle(shuffleState)
                }, icon = R.drawable.icon_shuffel, selected = shuffleState
        )

        AddIcon1(
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    viewModel.previous()
                }, icon = R.drawable.icon_prev_new
        )

        DrawPlayButton(viewModel = viewModel, modifier = Modifier)


        AddIcon1(
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    viewModel.next()
                }, icon = R.drawable.icon_next_new2
        )

        AddIcon(
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    repeatState = getRepeatState()
                    viewModel.setRepeatMode(repeatState.first)
                },
            icon = repeatState.second,
            selected = (repeatState.first != RepeatMode.REPEAT_NONE)
        )
    }


}


fun getRepeatState(): Pair<RepeatMode, Int> {
    index = (++index) % repeatArray.size
    return repeatArray[index]
}

@Composable
fun DrawPlayButton(viewModel: PlayerViewModel, modifier: Modifier) {
    val isPlayingState by viewModel.playBackUIState!!.isPlayingState.collectAsStateWithLifecycle()
    Log.d("DrawPlayButton", "isPlayingState: $isPlayingState")
    AddIcon2(
        modifier = modifier
            .size(100.dp),
        icon = if (isPlayingState) R.drawable.icon_pause_new
        else R.drawable.icon_play_new2,
        onClick = {
            if (isPlayingState) viewModel.pause() else viewModel.play()
        }
    )
}

@Preview(showBackground = false)
@Composable
fun DrawPlayerControlPreview() {
    DrawPlayerControl(
        viewModel = viewModel(),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .background(color = Color.White)
    )
}
