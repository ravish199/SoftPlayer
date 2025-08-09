package com.ravish.softplayer.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ravish.softplayer.Utils.formatMillisToMinuteSecond
import com.ravish.softplayer.ui.customview.CustomPlayerTrack
import com.ravish.softplayer.ui.customview.MyCustomSliderThumb
import com.ravish.softplayer.ui.theme.ActiveTrackColor
import com.ravish.softplayer.ui.theme.InactiveTrackColor
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

val thumbImage = mutableStateOf<Bitmap?>(null)
val title = mutableStateOf<String?>(null)
val singerName = mutableStateOf<String?>(null)

@OptIn(ExperimentalMaterial3Api::class)
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
        sliderStart = (currentPosition / 1000).toFloat()
        viewModel.songSeekValue = sliderStart
        Log.d("DrawPayerView", "sliderStart: ${sliderStart}")
    }

    Column(
        modifier = modifier
    ) {
        var sliderPosition by remember { mutableFloatStateOf(50f) }
        var interactionSource = remember { MutableInteractionSource() }

        DrawSongInfo(modifier = Modifier
            .fillMaxWidth()
            .weight(2f), title = title.value, singerName = singerName.value)
        val colors = SliderColors(
            thumbColor = Color.Red,
            activeTrackColor = ActiveTrackColor,
            activeTickColor = Color.Gray,
            inactiveTrackColor = InactiveTrackColor,
            inactiveTickColor = Color.Gray,
            disabledThumbColor = Color.Gray,
            disabledActiveTrackColor = Color.Gray,
            disabledActiveTickColor = Color.Gray,
            disabledInactiveTrackColor = Color.Gray,
            disabledInactiveTickColor = Color.Gray
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .weight(0.2f)
                    .padding(start = 10.dp),
                text = formatMillisToMinuteSecond(currentPosition),
                textAlign = TextAlign.Start,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black,
                ),
                fontWeight = FontWeight.Bold
            )


            val thumbHeight = 10.dp
            Slider(
                value = sliderStart.coerceAtLeast(0f),
                onValueChange = { sliderStart = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .weight(1f),
                valueRange = 0f..sliderEnd.coerceAtLeast(0f),
                steps = 0,
                interactionSource = interactionSource,
                colors = colors,
                onValueChangeFinished = {
                    viewModel.seekTo(sliderStart.toInt() * 1000L)
                },
                thumb = {
                 /*   SliderDefaults.Thumb(
                        interactionSource = interactionSource,
                        colors = colors,
                        thumbSize = DpSize(12.dp, 12.dp)
                    )*/
                    MyCustomSliderThumb(
                        interactionSource = interactionSource, // Pass the SAME source here
                        thumbColor = Color.Transparent,
                        iconColor = Color.White,
                        baseSize = 30.dp,
                        iconSize = 20.dp,
                        baseElevation = 0.dp,
                        iconElevation = 0.01.dp
                    )
                },
                track = { sliderState ->
             /*       SliderDefaults.Track(
                         colors = colors,
                        sliderState = sliderState,
                        thumbTrackGapSize = 0.dp,
                        trackInsideCornerSize = 0.dp
                    )*/
                    CustomPlayerTrack(
                        // Your new custom track composable
                        sliderState = sliderState,
                        activeTrackColor = ActiveTrackColor,  // From your theme
                        inactiveTrackColor = InactiveTrackColor, // From your theme
                        desiredTrackHeight = 8.dp,
                    )
                },

                )


            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .weight(0.2f)
                    .padding(end = 10.dp),
                text = formatMillisToMinuteSecond(totalDuration.coerceAtLeast(0)),
                textAlign = TextAlign.End,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black,
                ),
                fontWeight = FontWeight.Bold
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {


        }

    }
}

fun updatePlayerSong(bitmap: Bitmap, songItem: com.ravish.player.data.model.SongItem) {
    thumbImage.value = bitmap
    title.value = songItem.title
    singerName.value = songItem.artist
    updateBackground(bitmap)
    loadPlayerSong(songItem)
}


@Preview
@Composable
fun DrawPayerViewPreview() {
    DrawPayerView(
        viewModel = viewModel(),
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .fillMaxHeight()
    )
}



/*@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CustomPlayerTrackPreview() {
    CustomPlayerTrack( // Your new custom track composable
        sliderState = SliderState(),
        activeTrackColor = ActiveTrackColor,  // From your theme
        inactiveTrackColor = InactiveTrackColor, // From your theme
        desiredTrackHeight = 8.dp // Specify your desired track height here
    )
}*/
