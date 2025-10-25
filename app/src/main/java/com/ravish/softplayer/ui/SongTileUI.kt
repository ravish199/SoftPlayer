package com.ravish.softplayer.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ravish.player.data.model.SongItem
import com.ravish.softplayer.R
import com.ravish.softplayer.Utils
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

// import com.ravish.softplayer.ui.theme.ButtonBackgroundColor // Not used here
// import com.ravish.softplayer.ui.viewmodel.PlayerViewModel // Not directly used by this composable
// import kotlinx.coroutines.delay // Not needed

// These top-level mutable states are generally an anti-pattern for Compose.
// State should be scoped more locally (e.g., passed down or hoisted).
// For now, I'll leave them if your broader app structure relies on them, but consider refactoring.
var songListState = mutableStateOf(listOf<com.ravish.player.data.model.SongItem>())
var songUpdate = mutableStateOf<com.ravish.player.data.model.SongItem?>(null)

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("Recycle") // Utils.getImage might need careful handling if it returns raw Cursors/Bitmaps
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawSongTileUI(
    viewModel: PlayerViewModel,
    // modifier: Modifier, // This modifier was applied to the Row/Carousel. Apply to LazyRow now.
    audioList: List<SongItem>,
    modifier: Modifier = Modifier, // Add a default modifier for the LazyRow itself
    itemWidth: Dp = 180.dp, // Define a default width for each song tile
    itemHeight: Dp = 220.dp, // Define a height for the LazyRow and items
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 8.dp,
        vertical = 8.dp
    ), // Padding for the LazyRow content
) {

    var oldIndex = 0
    if (audioList.isNotEmpty()) {
        val lazyListState = rememberLazyListState() // Useful if you need to control scroll position

        val indexState by viewModel.songCategoryUIState!!.songIndexState.collectAsStateWithLifecycle()
        LaunchedEffect(indexState) {
            coroutineScope {
                launch {
                    if(abs(oldIndex - indexState) > 2) {
                        lazyListState.scrollToItem(indexState)
                    } else {
                        lazyListState.animateScrollToItem(indexState)
                    }
                }
            }
            oldIndex = indexState
        }

        LazyRow(
            modifier = modifier
                .fillMaxWidth() // LazyRow takes full available width
                .height(itemHeight), // Set a specific height for the row of tiles
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Spacing between items
            contentPadding = contentPadding, // Padding at the start/end and top/bottom of the content
            state = lazyListState, // Pass state if needed
        ) {
            items(
                items = audioList,
                key = { song -> song.id } // Provide a stable key for each item for performance
            ) { song ->
                // Now, for each 'song' in 'audioList', create a tile.
                // The Card will define the actual clickable area and appearance.
                var bitmap by remember(song.contentUri) { mutableStateOf<Bitmap?>(null) }
                var isLoading by remember(song.contentUri) { mutableStateOf(true) }

                // Load bitmap. Consider using a coroutine if Utils.getImage is slow,
                // or use an image loading library like Coil directly here.
                // For simplicity, direct call, but be mindful of performance on UI thread.
                // LaunchedEffect is better for side-effects like image loading.
                if (bitmap == null && isLoading) { // Basic attempt to load once
                    // This is still on UI thread. For real apps, use LaunchedEffect + withContext(Dispatchers.IO)
                    // or an image loading library.
                    bitmap = Utils.getImage(LocalContext.current, song.contentUri)
                    isLoading = false // Assume loading finishes quickly or fails.
                }

                val imageBitmap = remember(bitmap) { bitmap?.asImageBitmap() }

                Card(
                    modifier = Modifier
                        .width(itemWidth) // Each Card (song tile) has a fixed width
                        .fillMaxHeight()  // Card fills the height of the LazyRow
                        .padding(vertical = 4.dp) // Optional padding inside the spaced arrangement
                        .clickable {
                            imageBitmap?.asAndroidBitmap()?.let { androidBmp ->
                                viewModel.setPlayerBackground(androidBmp)
                                viewModel.playSelected(audioList.indexOf(song))
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    if (isLoading && imageBitmap == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator() // Show a loader
                        }
                    } else if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            modifier = Modifier.fillMaxSize(), // Image fills the Card
                            contentDescription = song.title ?: "Song artwork", // Accessibility
                            contentScale = ContentScale.Crop // Crop is usually good for album art
                        )
                    } else {
                        // Placeholder if bitmap is null and not loading (e.g., loading failed or no art)
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.empty_song_list), // Replace with a generic music note or placeholder
                                contentDescription = "No artwork available",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize(0.6f) // Smaller placeholder
                            )
                        }
                    }
                }
            }
        }
    } else {
        // Display when the audioList is empty
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


@RequiresApi(Build.VERSION_CODES.Q)
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
        DrawSongTileUI(
            viewModel = viewModel(),
            audioList = sampleSongs,
            itemWidth = 160.dp,
            itemHeight = 200.dp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = false, widthDp = 360, heightDp = 240)
@Composable
fun DrawSongTileUIPreview_LazyRow_Empty() {
    MaterialTheme {
        DrawSongTileUI(
            viewModel = viewModel(),
            audioList = emptyList(),
            itemHeight = 200.dp
        )
    }
}


// --- Helper Composables (AddSongImage and updateSongList seem less relevant to DrawSongTileUI directly) ---

// AddSongImage seems like a separate utility. If used as a placeholder within the tile,
// it should be integrated into the LazyRow item's content.
@Composable
fun AddSongImage(modifier: Modifier, songItem: com.ravish.player.data.model.SongItem) {
    // This AsyncImage setup is more typical for loading images.
    // Consider using Coil directly in DrawSongTileUI item if Utils.getImage is problematic.
    AsyncImage(
        modifier = modifier,
        model = songItem.contentUri, // Coil can handle Uris
        contentDescription = "Song Artwork for ${songItem.title}",
        error = painterResource(id = R.drawable.empty_song_list), // Placeholder on error
        placeholder = painterResource(id = R.drawable.empty_song_list), // Placeholder while loading
        contentScale = ContentScale.Crop
    )
}

// updateSongList function modifies a global state variable.
// This is generally not recommended in Compose for managing UI state.
// Prefer passing lists as parameters or using ViewModel with StateFlow.
fun updateSongList(songList: List<com.ravish.player.data.model.SongItem>) {
    songListState.value = songList
}

// Preview for AddSongImage
@Preview(showBackground = false)
@Composable
fun AddSongImagePreview() {
    AddSongImage(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        songItem = SongItem(100L, "Sample", "Artist", "Album", 100L, "", Uri.EMPTY)
    )
}

