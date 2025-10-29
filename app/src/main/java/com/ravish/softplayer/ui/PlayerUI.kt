package com.ravish.softplayer.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravish.player.data.model.SongItem
import com.ravish.softplayer.Utils
import com.ravish.softplayer.data.model.SliderUIState
import com.ravish.softplayer.data.model.uistate.EqualizerUIState
import com.ravish.softplayer.data.model.uistate.MediaUpdateUIState
import com.ravish.softplayer.data.model.uistate.PlayBackUIState
import com.ravish.softplayer.data.model.uistate.SongCategoryUIState
import com.ravish.softplayer.data.model.uistate.SongInfoUIState
import com.ravish.softplayer.data.model.uistate.TrackListUIState
import com.ravish.softplayer.ui.playercontrol.DrawPlayerControl
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@Composable
fun DrawPlayerUI(viewModel: PlayerViewModel) {

    val audioList by viewModel.mediaUpdateUIState.mediaItemsUpdateState.collectAsStateWithLifecycle()
    val backgroundImage by viewModel.songInfoUIState.playerBackgroundState.collectAsStateWithLifecycle()
    val categoryName by viewModel.songCategoryUIState.categoryNameState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.background(Color.Black)) {
        Log.d("DrawPlayerUI", "DrawPlayerUI")
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .blur(30.dp),
            bitmap = Utils.getImage(LocalContext.current, backgroundImage),
            contentDescription = "Song Name",
            contentScale = ContentScale.FillBounds,
            alpha = 1f
        )
        OverLayView(modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            val modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)



            Box(
                modifier = Modifier
                    .weight(7f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    DrawSongHeader(
                        viewModel = viewModel,
                        modifier = modifier.weight(1f),
                        categoryName
                    )

                    DrawSongTileUI(
                        viewModel = viewModel,
                        modifier = Modifier
                            .weight(3f)
                            .padding(0.dp),
                        audioList = audioList
                    )
                    DrawPayerView(viewModel, modifier = modifier.weight(3f))
                }

            }

            DrawPlayerControl(
                viewModel,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1.5f)
                    .padding(bottom = 80.dp),
            )
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun DrawPlayerUIPreview() {
    DrawPlayerUI(
        viewModel = FakePlayerViewModel(
        )
    )
}

class FakePlayerViewModel @Inject constructor(
) : PlayerViewModel() {
    init {
        // Initialize the UI state objects with fake data
        songCategoryUIState = SongCategoryUIState(
            categoryNameState = MutableStateFlow("All Songs").asStateFlow(),
            totalCountState = MutableStateFlow(10).asStateFlow(),
            songIndexState = MutableStateFlow(0).asStateFlow()
        )
        songInfoUIState = SongInfoUIState(
            songTitleState = MutableStateFlow("Song Title").asStateFlow(),
            albumState = MutableStateFlow("Album Name").asStateFlow(),
            artistsState = MutableStateFlow("Artist Name").asStateFlow(),
            imageUriState = MutableStateFlow(null).asStateFlow(),
            playerBackgroundState = MutableStateFlow(null).asStateFlow()
        )
        sliderUIState = SliderUIState(
            totalDurationState = MutableStateFlow(240000L).asStateFlow(), // 4 minutes
            currentPositionState = MutableStateFlow(60000L).asStateFlow() // 1 minute
        )
        mediaUpdateUIState = MediaUpdateUIState(
            mediaItemsUpdateState = MutableStateFlow(
                listOf(
                    SongItem(
                        1, "Song 1", "Artist 1",
                        "Album 1", 240000, "", Uri.EMPTY
                    )
                )
            )
                .asStateFlow()
        )
        playBackUIState = PlayBackUIState(
            isPlayingState = MutableStateFlow(false).asStateFlow(),
            playEndedState = MutableStateFlow(false).asStateFlow(),
            playingTrack = MutableStateFlow(null).asStateFlow()
        )

        trackListUIState = TrackListUIState(
            openTrackListStatus = MutableStateFlow(false).asStateFlow(),
            filterTrack = MutableStateFlow("")
        )

        equalizerUIState = EqualizerUIState(
            openEqualizerState = MutableStateFlow(false).asStateFlow(),
            enableEqualizerState = MutableStateFlow(false).asStateFlow(),
            updatePresetBand = MutableStateFlow<List<Float>>(emptyList()).asStateFlow(),
            presetName = MutableStateFlow("Flat").asStateFlow(),
            enableReverbState = MutableStateFlow(false).asStateFlow(),
            currentReverb = MutableStateFlow(0).asStateFlow(),
            updateReverb = MutableStateFlow(0).asStateFlow()
        )
    }
}