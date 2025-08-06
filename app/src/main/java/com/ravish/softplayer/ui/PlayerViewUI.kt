package com.ravish.softplayer.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ravish.softplayer.R
import com.ravish.softplayer.Utils.formatMillisToMinuteSecond
import com.ravish.softplayer.ui.theme.ButtonBackgroundColor
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

val thumbImage = mutableStateOf<Bitmap?>(null)


@Composable
fun DrawPayerView(viewModel: PlayerViewModel, modifier: Modifier) {

    var totalDuration by remember { mutableStateOf(0L) }
    var currentPosition by remember { mutableStateOf(0L) }
    var sliderStart = 0f
    var sliderEnd = 0f

    viewModel.getTotalDuration()?.collectAsStateWithLifecycle()?.let {
        Log.d("DrawPayerView", "totalDuration: ${it.value}")
        totalDuration = it.value
        sliderEnd = (totalDuration / 1000).toFloat()

        Log.d("DrawPayerView", "sliderEnd: ${sliderEnd}")
    }

    viewModel.currentPosition()?.collectAsStateWithLifecycle()?.let {
        Log.d("DrawPayerView", "currentPosition: ${it.value}")
        currentPosition = it.value
        sliderStart= (currentPosition / 1000).toFloat()
        viewModel.songSeekValue = sliderStart
        Log.d("DrawPayerView", "sliderStart: ${sliderStart}")
    }

    Column(
        modifier = modifier.background(ButtonBackgroundColor)
    ) {
        var sliderPosition by remember { mutableFloatStateOf(50f) }
        Box() {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                bitmap = (thumbImage.value ?: BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    R.drawable.app_background
                )).asImageBitmap(),
                contentDescription = "Song Name",
                contentScale = ContentScale.FillBounds
            )
        }

        Slider(
            value = sliderStart.coerceAtLeast(0f),
            onValueChange = { sliderStart = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            valueRange = 0f..sliderEnd.coerceAtLeast(0f),
            steps = 0,
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Cyan,
                inactiveTrackColor = Color.Red,
            ),
            onValueChangeFinished = {
                viewModel.seekTo(sliderStart.toInt() * 1000L)
            }
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 10.dp)
                    .align(alignment = Alignment.TopStart),
                text = formatMillisToMinuteSecond(currentPosition),
                textAlign = TextAlign.Start,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 20.sp,

                    color = Color.Cyan,
                    shadow = Shadow(
                        color = Color.Red,
                        blurRadius = 20f
                    )
                )
            )

            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 10.dp)
                    .align(alignment = Alignment.TopEnd),
                text = formatMillisToMinuteSecond(totalDuration.coerceAtLeast(0)),
                textAlign = TextAlign.End,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Cyan,
                    shadow = Shadow(
                        color = Color.Red,
                        blurRadius = 20f
                    )
                )
            )
        }

    }
}

fun updatePlayerSong(bitmap: Bitmap, songItem: com.ravish.player.data.model.SongItem) {
    thumbImage.value = bitmap
    updateBackground(bitmap)
    loadPlayerSong(songItem)
}


@Preview
@Composable
fun DrawPayerViewPreview() {
    DrawPayerView(
        viewModel = viewModel(),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
}
