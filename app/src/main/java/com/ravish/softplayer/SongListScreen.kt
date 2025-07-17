package com.ravish.softplayer

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment

@Composable
fun SongListScreen(
    songs: List<SongItem>,
    onSongClick: (SongItem) -> Unit,
    modifier: Modifier = Modifier
) {
    if (songs.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No songs found.")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(
                items = songs,
                key = { song -> song.id } // Important for performance and state preservation
            ) { song ->
                SongListItem(song = song, onSongClick = onSongClick)
            }
        }
    }
}