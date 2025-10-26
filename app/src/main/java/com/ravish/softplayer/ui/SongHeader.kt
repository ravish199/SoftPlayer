package com.ravish.softplayer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun DrawSongHeader(viewModel: PlayerViewModel, modifier: Modifier, categoryName: String? = null) {

    val songIndex by viewModel.songCategoryUIState!!.songIndexState.collectAsStateWithLifecycle()
    val totalSongs by viewModel.songCategoryUIState!!.totalCountState.collectAsStateWithLifecycle()


    var listMode by remember { mutableStateOf(false) }

    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (header, equalizer, trackList) = createRefs()
        Column(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(header) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
        ) {
            Text(
                modifier = Modifier.padding(
                    5.dp
                ),
                text = categoryName ?: "",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.padding(
                    5.dp
                ),
                text = "${songIndex + 1}/$totalSongs" ?: "0",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

        }

        AddIcon2(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(equalizer) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            icon = R.drawable.frequncry_icon,
            onClick = {
                viewModel.updateEqualizeView()
            },
            isEnabled = true
        )

        AddIcon2(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(trackList) {
                    end.linkTo(equalizer.start)
                    bottom.linkTo(parent.bottom)
                },
            icon = R.drawable.ic_track_list,
            onClick = {
                viewModel.openTrackList()
            },
            isEnabled = true
        )

    }
}

@Composable
fun DrawEqualizerIcon() {

}

@Composable
fun DrawSongInfo(viewModel: PlayerViewModel, modifier: Modifier) {

    val songTitle = viewModel.songInfoUIState!!.songTitleState.collectAsStateWithLifecycle().value
    val artistName = viewModel.songInfoUIState!!.artistsState.collectAsStateWithLifecycle().value


    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(
                    10.dp
                )
                .fillMaxWidth(), text = songTitle ?: "",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .padding(
                    10.dp
                )
                .fillMaxWidth(),
            text = artistName ?: "",
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
@Preview(showBackground = false)
fun DrawSongHeaderPreview() {
    Column {
        DrawSongHeader(
            viewModel = viewModel(),
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
        )
        DrawSongInfo(
            viewModel = viewModel(), modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
        )
    }

}