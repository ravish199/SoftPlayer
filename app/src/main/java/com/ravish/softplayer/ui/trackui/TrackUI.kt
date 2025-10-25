package com.ravish.softplayer.ui.trackui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravish.player.data.model.SongItem
import com.ravish.softplayer.R
import com.ravish.softplayer.Utils
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.theme.HighLightColor
import com.ravish.softplayer.ui.theme.TrackAlbumTextColor
import com.ravish.softplayer.ui.theme.TrackArtistTextColor
import com.ravish.softplayer.ui.theme.TrackTitleTextColor
import com.ravish.softplayer.ui.theme.Typography
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun TrackUI(
    viewModel: PlayerViewModel,
    modifier: Modifier,
    audioList: List<SongItem>,
    itemHeight: Dp = 60.dp,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 8.dp,
        vertical = 8.dp
    )
) {
    var songList by remember { mutableStateOf(audioList) }
    val filterTrack by viewModel.trackListUIState.filterTrack.collectAsStateWithLifecycle()

    LaunchedEffect(filterTrack) {
        songList = audioList.filter { it.title.contains(filterTrack, ignoreCase = true) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 30.dp)
    ) {

        SearchBar(viewModel = viewModel,
            modifier = Modifier.fillMaxWidth())

        if (songList.isNotEmpty()) {
            val lazyListState = rememberLazyListState()

            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = contentPadding,
                state = lazyListState,
            ) {
                items(
                    items = songList,
                    key = { song -> song.id }
                ) { song ->
                    var bitmap by remember(song.contentUri) { mutableStateOf<Bitmap?>(null) }
                    var isLoading by remember(song.contentUri) { mutableStateOf(true) }
                    if (bitmap == null && isLoading) { // Basic attempt to load once
                        bitmap = Utils.getImage(LocalContext.current, song.contentUri)
                    }

                    val imageBitmap = remember(bitmap) { bitmap?.asImageBitmap() }
                    TrackItem(modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            Log.d("TrackUI", "TrackItem: ${song.title}")
                            viewModel.playSelected(audioList.indexOf(song))
                        }, itemHeight = itemHeight, song = song, imageBitmap = imageBitmap)
                }
            }
        } else {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(itemHeight) // Match potential height of the list
                    .padding(contentPadding), // Use similar padding
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(0.8f), // Adjust size as needed
                    painter = painterResource(R.drawable.empty_song_list),
                    contentDescription = "Song list is empty",
                    contentScale = ContentScale.Fit,
                    alpha = 0.7f
                )
            }
        }
    }
}

@Composable
fun TrackItem(
    modifier: Modifier,
    itemHeight: Dp,
    song: SongItem,
    imageBitmap: ImageBitmap?,
    isPlaying: Boolean = false
) {
    Row(
   modifier = modifier
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                modifier = Modifier.weight(1f),
                contentDescription = song.title ?: "Song artwork",
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(5f)
                    .height(itemHeight)
            ) {
                val textModifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                Text(
                    modifier = textModifier,
                    text = song.title,
                    style = Typography.labelLarge,
                    color = if (isPlaying) HighLightColor else TrackTitleTextColor
                )

                Text(
                    modifier = textModifier,
                    text = song.album ?: "",
                    style = Typography.labelMedium,
                    color = if (isPlaying) HighLightColor else TrackAlbumTextColor
                )

                Text(
                    modifier = textModifier,
                    text = song.artist ?: "",
                    style = Typography.labelSmall,
                    color = if (isPlaying) HighLightColor else TrackArtistTextColor
                )
            }

        } else {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empty_song_list),
                    contentDescription = "No artwork available",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(0.6f)
                )
            }
        }
    }
}


/*@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = false, widthDp = 360, heightDp = 240)
@Composable
fun TrackUIPreview_LazyColumn_NotEmpty() {
    MaterialTheme {
        TrackUI(
            modifier = Modifier.fillMaxSize(),
            audioList = emptyList()
        )
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = false, widthDp = 360, heightDp = 240)
@Composable
fun DrawSongTileUIPreview_LazyRow_NotEmpty() {
    val sampleSongs = listOf(
        SongItem(
            1L,
            "Song Title 1",
            "Artist 1",
            "Album 1",
            180000L,
            "Fake Uri 1",
            Uri.parse("content://media/external/audio/media/1")
        ),
        SongItem(
            2L,
            "Song Title 2",
            "Artist 2",
            "Album 2",
            240000L,
            "Fake Uri 2",
            Uri.parse("content://media/external/audio/media/2")
        ),
        SongItem(
            3L,
            "A Very Long Song Title That Might Wrap Or Be Truncated",
            "Artist 3",
            "Album 3",
            200000L,
            "Fake Uri 3",
            Uri.parse("content://media/external/audio/media/3")
        ),
        SongItem(
            4L,
            "Song 4",
            "Artist 4",
            "Album 4",
            150000L,
            "Fake Uri 4",
            Uri.parse("content://media/external/audio/media/4")
        )
    )
    MaterialTheme {
        TrackUI(
            viewModel = FakePlayerViewModel(),
            modifier = Modifier.fillMaxSize(),
            audioList = sampleSongs
        )
    }
}