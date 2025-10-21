package com.ravish.softplayer.ui.equalizerview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.ui.customview.CustomPlayerTrack
import com.ravish.softplayer.ui.customview.MyCustomSliderThumb
import com.ravish.softplayer.ui.theme.ActiveTrackColor
import com.ravish.softplayer.ui.theme.ButtonContainerColorSemiTransparent
import com.ravish.softplayer.ui.theme.ButtonTextColor
import com.ravish.softplayer.ui.theme.InactiveTrackColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreampSlider(modifier: Modifier) {
    var currentSliderSeconds by remember { mutableFloatStateOf(0f) }
    var currentPosition by remember { mutableFloatStateOf(0f) }
    var sliderTotalSeconds by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val sliderMaxValue = 100f
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

    LaunchedEffect(currentPosition, isDragging, sliderMaxValue) {
        if (!isDragging) {
            val vmPositionSeconds = (currentPosition / 1000f)
            // CoerceIn is crucial to keep the thumb within valid bounds.
            currentSliderSeconds = vmPositionSeconds.coerceIn(0f, sliderMaxValue)
            // Log.d("SliderView", "VM updated slider to: $currentSliderSeconds s (from $currentPositionMs ms)")
        }
    }

    LaunchedEffect(sliderTotalSeconds, isDragging) {
        if (!isDragging) { // Avoid interference if a drag is somehow active during duration change
            currentSliderSeconds = currentSliderSeconds.coerceIn(0f, sliderMaxValue)
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
        valueRange = 0f..sliderMaxValue.coerceAtLeast(0f),
        steps = 0,
        interactionSource = interactionSource,
        colors = colors,
        onValueChangeFinished = {
            isDragging = false
            //viewModel.seekTo((currentSliderSeconds * 1000).toLong())
        },
        thumb = {
            MyCustomSliderThumb(
                interactionSource = interactionSource, // Pass the SAME source here
                thumbColor = Color.Transparent,
                iconColor = ActiveTrackColor,
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
                inactiveTrackColor = ButtonContainerColorSemiTransparent, // From your theme
                desiredTrackHeight = 5.dp,
            )
        },

        )
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreampSliderPreview() {
PreampSlider(modifier = Modifier.fillMaxWidth())
}