package com.ravish.softplayer.ui.sliderview

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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


@Composable
fun SliderView(viewModel: PlayerViewModel, modifier: Modifier) {
    Log.d("SliderView", "SliderView")
    val totalDuration by viewModel.sliderUIState!!.totalDurationState.collectAsStateWithLifecycle(
        initialValue = 0L
    )
    val currentPosition by viewModel.sliderUIState!!.currentPositionState.collectAsStateWithLifecycle()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Log.d("SliderView", "Row")
        Log.d("SliderView", "totalDuration:$totalDuration")
        Log.d("SliderView", "currentPosition:$currentPosition")

        DrawCurrentTimeText(
            currentPosition,
            modifier = Modifier
                .wrapContentSize()
                .weight(0.2f)
                .padding(start = 10.dp)
        )

        DrawSlider(
            viewModel = viewModel,
            totalDuration = totalDuration,
            currentPosition = currentPosition,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .weight(1f)
        )

        DrawSongDurationText(
            modifier = Modifier
                .wrapContentSize()
                .weight(0.2f)
                .padding(end = 10.dp), totalDuration
        )


    }
}

@Composable
fun DrawCurrentTimeText(currentPosition: Long, modifier: Modifier) {
    Log.d("DrawCurrentTimeText", "DrawCurrentTimeText")

    Text(
        modifier = modifier,
        text = formatMillisToMinuteSecond((currentPosition / 1000)),
        textAlign = TextAlign.Start,
        maxLines = 1,
        style = TextStyle(
            fontSize = 14.sp,
            color = Color.Black,
        ),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun DrawSongDurationText(modifier: Modifier, totalDuration: Long) {
    Log.d("DrawSongDurationText", "totalDuration:$totalDuration")

    Text(
        modifier = modifier,
        text = formatMillisToMinuteSecond((totalDuration / 1000)),
        textAlign = TextAlign.End,
        maxLines = 1,
        style = TextStyle(
            fontSize = 14.sp,
            color = Color.Black,
        ),
        fontWeight = FontWeight.Bold
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawSlider(
    viewModel: PlayerViewModel,
    totalDuration: Long,
    currentPosition: Long,
    modifier: Modifier
) {
    Log.d("DrawSlider", "DrawSlider")
    val interactionSource = remember { MutableInteractionSource() }

    var currentSliderSeconds by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val sliderTotalSeconds = remember(totalDuration) {
        (totalDuration / 1000f).coerceAtLeast(0f)
    }
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

    LaunchedEffect(currentPosition, isDragging, sliderTotalSeconds) {
        if (!isDragging) {
            val vmPositionSeconds = (currentPosition / 1000f)
            // CoerceIn is crucial to keep the thumb within valid bounds.
            currentSliderSeconds = vmPositionSeconds.coerceIn(0f, sliderTotalSeconds)
            // Log.d("SliderView", "VM updated slider to: $currentSliderSeconds s (from $currentPositionMs ms)")
        }
    }

    LaunchedEffect(sliderTotalSeconds, isDragging) {
        if (!isDragging) { // Avoid interference if a drag is somehow active during duration change
            currentSliderSeconds = currentSliderSeconds.coerceIn(0f, sliderTotalSeconds)
            // Log.d("SliderView", "Duration changed. Coerced currentSliderSeconds: $currentSliderSeconds s")
        }
    }


    Slider(
        value = currentSliderSeconds,
        onValueChange = {
            isDragging = true
            currentSliderSeconds = it
        },
        modifier = modifier,
        valueRange = 0f..sliderTotalSeconds.coerceAtLeast(0f),
        steps = 0,
        interactionSource = interactionSource,
        colors = colors,
        onValueChangeFinished = {
            isDragging = false
            viewModel.seekTo((currentSliderSeconds * 1000).toLong())
        },
        thumb = {
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
            CustomPlayerTrack(
                // Your new custom track composable
                sliderState = sliderState,
                activeTrackColor = ActiveTrackColor,  // From your theme
                inactiveTrackColor = InactiveTrackColor, // From your theme
                desiredTrackHeight = 8.dp,
            )
        },

        )
}

@Composable
@Preview
fun SliderViewPreview() {
    SliderView(viewModel = viewModel(), modifier = Modifier)
}