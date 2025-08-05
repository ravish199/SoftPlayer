package com.ravish.softplayer.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ravish.softplayer.R
import com.ravish.softplayer.data.model.RepeatMode

val repeatArray = arrayOf(
    Pair(RepeatMode.REPEAT_NONE, R.drawable.icon_repeat_none),
    Pair(RepeatMode.REPEAT_ALL, R.drawable.icon_repeat_all),
    Pair(RepeatMode.REPEAT_ONE, R.drawable.icon_repeat_one)
)
var index = 0

@Composable
fun DrawPlayerControl(modifier: Modifier) {
    var shuffleState by remember { mutableStateOf(false) }
    var repeatState by remember { mutableStateOf(Pair(RepeatMode.REPEAT_ONE, R.drawable.icon_repeat_one)) }

    Row(
        modifier = modifier.background(Color.Transparent),
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

            }, icon = R.drawable.icon_play_cirlce, selected = true)
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
}

fun getRepeatState(): Pair<RepeatMode, Int> {
    index = (++index) % repeatArray.size
    return repeatArray[index]
}

@Preview
@Composable
fun DrawPlayerControlPreview() {
    DrawPlayerControl(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
    )
}
