package com.ravish.softplayer.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ravish.softplayer.R
import com.ravish.player.data.model.SongItem
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

val backgroundImage = mutableStateOf<Bitmap?>(null)

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DrawPlayerUI(viewModel: PlayerViewModel) {


    Box(modifier = Modifier.background(Color.Black)) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .blur(30.dp),
             bitmap = (backgroundImage.value?: BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.app_background)).asImageBitmap(),
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
          //  DrawEquilizerUI(modifier = modifier.weight(1.5f).padding(top = 50.dp))

          //  DrawSongGroupUI(modifier = modifier.weight(1f))

            DrawSongHeader(modifier = modifier.weight(1f), viewModel.categoryName, viewModel.totalSongs)

            DrawSongTileUI(viewModel,
                modifier = Modifier.weight(2.5f).
                padding(0.dp))

            DrawPayerView(viewModel, modifier = modifier.weight(2.5f))


            DrawPlayerControl(viewModel,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1.5f)
                    .padding(bottom = 100.dp),
            )
        }
    }
}

fun updateSongs(songList: List<com.ravish.player.data.model.SongItem>) {
    updateSongList(songList)
}

fun updateBackground(bitmap: Bitmap?) {
backgroundImage.value = bitmap
}


@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun DrawPlayerUIPreview() {
    DrawPlayerUI(viewModel = viewModel())
}