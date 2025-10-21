package com.ravish.softplayer.ui.sliderview

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.ui.customview.CustomPlayerTrack
import com.ravish.softplayer.ui.customview.MyCustomSliderThumb
import com.ravish.softplayer.ui.theme.ActiveTrackColor
import com.ravish.softplayer.ui.theme.InactiveTrackColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderView2(
    modifier: Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Slider(
        modifier = modifier
            .wrapContentSize(),
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        valueRange = valueRange,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = SliderDefaults.colors(
            thumbColor = Color.Red,
            activeTrackColor = Color.Red,
            inactiveTrackColor = Color.Red.copy(alpha = 0.3f)
        ),
        thumb = {
            MyCustomSliderThumb(
                interactionSource = interactionSource, // Pass the SAME source here
                thumbColor = ActiveTrackColor,
                iconColor = ActiveTrackColor,
                baseSize = 16.dp,
                iconSize = 12.dp,
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
                desiredTrackHeight = 6.dp,
            )
        },
    )

}