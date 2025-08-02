package com.ravish.softplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.DrawPlayerControl
import com.ravish.softplayer.ui.theme.AppBackgroundColor

@Composable
fun DrawPlayerUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundColor),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
        DrawEquilizerUI(modifier = modifier.weight(1f))

        DrawSongGroupUI(modifier = modifier.weight(1f))

        DrawSongTileUI(modifier = modifier.weight(3f))

        DrawPlayerControl(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 10.dp),
        )
    }
}


@Preview
@Composable
fun DrawPlayerUIPreview() {
    DrawPlayerUI()
}