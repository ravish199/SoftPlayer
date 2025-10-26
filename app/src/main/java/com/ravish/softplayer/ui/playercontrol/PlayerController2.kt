package com.ravish.softplayer.ui.playercontrol

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravish.player.data.model.SongItem
import com.ravish.softplayer.R
import com.ravish.softplayer.Utils
import com.ravish.softplayer.Utils.drawShadow
import com.ravish.softplayer.data.model.ShadowType
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.sliderview.SliderView
import com.ravish.softplayer.ui.theme.TrackAlbumTextColor
import com.ravish.softplayer.ui.theme.TrackArtistTextColor
import com.ravish.softplayer.ui.theme.TrackTitleTextColor
import com.ravish.softplayer.ui.theme.Typography
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel


@Composable
fun PlayerControl2(
    viewModel: PlayerViewModel,
    modifier: Modifier
) {
    val songTitle by viewModel.songInfoUIState.songTitleState.collectAsStateWithLifecycle()
    val artistName by viewModel.songInfoUIState.artistsState.collectAsStateWithLifecycle()
    val albumName by viewModel.songInfoUIState.albumState.collectAsStateWithLifecycle()
    val imageUri by viewModel.songInfoUIState.imageUriState.collectAsStateWithLifecycle()

    Column(   modifier = modifier.drawShadow(
        shadowType = ShadowType.TOP,
        shadowWidth = 5.dp
    )) {

        SliderView(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            onlySlider = true
        )

        Row() {
                Image(
                    bitmap = Utils.getImageSmall(LocalContext.current, imageUri).asImageBitmap(),
                    modifier = Modifier.weight(2f).fillMaxHeight()
                        .padding(start = 8.dp),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )

            Column(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxHeight()
            ) {
                val textModifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)

                Text(
                    modifier = textModifier.padding(top = 2.dp),
                    text = songTitle,
                    maxLines = 1,
                    style = Typography.labelLarge,
                    color = TrackTitleTextColor
                )


                Text(
                    modifier = textModifier,
                    text = albumName,
                    maxLines = 1,
                    style = Typography.labelMedium,
                    color = TrackAlbumTextColor
                )

                Text(
                    modifier = textModifier,
                    text = artistName,
                    maxLines = 1,
                    style = Typography.labelSmall,
                    color = TrackArtistTextColor
                )

            }
            DrawPlayButton(
                viewModel = viewModel,
                modifier = Modifier.padding(start = 2.dp, top = 2.dp, bottom =2.dp)
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = false)
@Composable
fun TrackDetailsPreview() {
    val songItem = SongItem(
        1L,
        "Song Title 1",
        "Artist 1",
        "Album 1",
        180000L,
        "Fake Uri 1",
        Uri.parse("content://media/external/audio/media/1")
    )

    MaterialTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color = Color.White),
            contentAlignment = Alignment.Center) {
            PlayerControl2(
                viewModel = FakePlayerViewModel(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .drawShadow(shadowType = ShadowType.TOP, shadowWidth = 10.dp)
            )
        }
    }

}
