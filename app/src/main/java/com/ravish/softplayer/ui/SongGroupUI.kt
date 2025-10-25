package com.ravish.softplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ravish.softplayer.R
import com.ravish.softplayer.data.model.EquilizerSelection
import com.ravish.softplayer.data.model.MoodSelection
import com.ravish.softplayer.ui.theme.ButtonBackgroundColor
import com.ravish.softplayer.ui.theme.SongsTileBackgroundColor

@Composable
fun DrawSongGroupUI(modifier: Modifier) {
    var moodType by remember { mutableStateOf(MoodSelection.HAPPY) }
    Row(
        modifier = modifier.background(ButtonBackgroundColor),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AddIconWithLabel(modifier = Modifier.wrapContentSize().clickable {
            moodType = MoodSelection.HAPPY
        }, selected = moodType == MoodSelection.HAPPY, icon = R.drawable.icon_happy, label = "Happy")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().clickable {
            moodType = MoodSelection.MOTIVATION
        },selected = moodType == MoodSelection.MOTIVATION,icon =  R.drawable.icon_motivation, label = "Motivation")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().clickable {
            moodType = MoodSelection.DANCE
        }, selected = moodType == MoodSelection.DANCE, icon = R.drawable.icon_dance, label = "Dance")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().clickable {
            moodType = MoodSelection.CLASSIC
        }, selected = moodType == MoodSelection.CLASSIC ,icon = R.drawable.icon_classic, label = "Classic")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().clickable {
            moodType = MoodSelection.SAD
        }, selected = moodType == MoodSelection.SAD, icon = R.drawable.icon_sad, label = "Sad")
    }
}



@Composable
@Preview(showBackground = false)
fun DrawSongGroupUIPreview() {
    DrawEquilizerUI(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
    )
}
