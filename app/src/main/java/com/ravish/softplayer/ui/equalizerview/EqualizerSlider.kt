package com.ravish.softplayer.ui.equalizerview

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.ravish.softplayer.AppConstant.Companion.NUMBER_OF_SLIDERS
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.sliderview.SliderView2
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.ravish.softplayer.ui.theme.ActiveTrackColor
import com.ravish.softplayer.ui.theme.ActiveTrackColor_Semi_transparent
import com.ravish.softplayer.ui.theme.InactiveTrackColor
import com.ravish.softplayer.ui.theme.InactiveTrackColor_Semi_transparent

@SuppressLint("UnusedBoxWithConstraintsScope")
@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerSlider(
    modifier: Modifier,
    viewModel: PlayerViewModel,
) {
    BoxWithConstraints(modifier = modifier.background(color = Color.White)) {
        val maxWidth = min(maxWidth, maxHeight)
        Box(modifier = Modifier.size(maxWidth).align(alignment = Alignment.Center)) {
            val eqEnabled by viewModel.enableEqualizerState.collectAsStateWithLifecycle()
            val eqModifier = Modifier
                .size(maxWidth * 0.8f)
                .align(alignment = Alignment.Center)


            addCenterLine(modifier = eqModifier)

            addEqualizerValueLables(
                modifier = Modifier
                    .width(maxWidth * 0.1f)
                    .height(maxWidth * 0.8f)
                    .align(alignment = Alignment.CenterStart),
                eqEnabled
            )

            addEqualizerFrequencyLabels(
                modifier = Modifier
                    .width(maxWidth * 0.8f)
                    .height(maxWidth * 0.1f)
                    .align(alignment = Alignment.BottomCenter),
                eqEnabled
            )

            addEqualizerSliders(modifier = eqModifier, viewModel = viewModel)


        }
    }
}

@Composable
fun addEqualizerValueLables(modifier: Modifier, enable: Boolean) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        val equalizerBandValues = LocalResources.current.getStringArray(R.array.equalizer_band_values)
        for (i in 0..equalizerBandValues.size - 1) {
            addValueLabelText(equalizerBandValues[i], enable)
        }
    }
}

@Composable
fun addCenterLine(modifier: Modifier) {
    Canvas(
        modifier = modifier
    ) {
        // 'size' provides the width and height of the Canvas
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Calculate the Y position for the center line
        val centerY = canvasHeight / 2

        // Draw a horizontal line from the left edge to the right edge at the center
        drawLine(
            color = ActiveTrackColor_Semi_transparent, // Use a color that fits your theme
            start = Offset(x = 0f, y = centerY),
            end = Offset(x = canvasWidth, y = centerY),
            strokeWidth = 2.dp.toPx(), // Make the line 2dp thick
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun addEqualizerSliders(modifier: Modifier, viewModel: PlayerViewModel) {
    Column(
        modifier = modifier
            .graphicsLayer {
                rotationZ = -90f
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            },
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Use a Box to contain the rotated slider
        val eqEnabled by viewModel.enableEqualizerState.collectAsStateWithLifecycle()
      //  val eqBandsLevel by viewModel.savedEqualizerBandlevels.collectAsStateWithLifecycle()
val selectedPreset by viewModel.updatePresetBand.collectAsStateWithLifecycle()

        for (i in 0..NUMBER_OF_SLIDERS-1) {
            val sliderValues =
                remember {
                    mutableFloatStateOf(
                        if (eqEnabled) selectedPreset[i] else 0f
                    )
                }
            val interactionSource = remember { MutableInteractionSource() }
            SliderView2(
                modifier = Modifier.fillMaxWidth().weight(1f),
                value = sliderValues.floatValue,
                onValueChange = { newValue ->
                    sliderValues.floatValue = newValue
                },
                onValueChangeFinished = {
                    viewModel.setBandLevel(i, sliderValues.floatValue)
                    Log.d("VerticalSlider", "slider value $i: ${sliderValues.floatValue}")
                },
                0f..1f,
                eqEnabled,
                interactionSource
            )

            LaunchedEffect(selectedPreset) {
                if (selectedPreset.isNotEmpty()) {
                    sliderValues.floatValue = selectedPreset[i]
                }
            }
        }

    }
}

@Composable
fun addEqualizerFrequencyLabels(modifier: Modifier, enable: Boolean) {
    val equalizerBandLevels = LocalResources.current.getStringArray(R.array.equalizer_band_levels)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
       for(i in 0..equalizerBandLevels.size-1) {
           addFrequencyLabelText(equalizerBandLevels[i], enable)
    }
}
}

@Composable
fun addValueLabelText(label: String, enable: Boolean=false) {
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = label,
        style = TextStyle(fontSize = 12.sp),
        color = if(enable) ActiveTrackColor else InactiveTrackColor,
        textAlign = TextAlign.End
    )
}

@Composable
fun addFrequencyLabelText(label: String, enable: Boolean=false) {
    Text(
        modifier = Modifier,
        text = label,
        style = TextStyle(fontSize = 12.sp),
        color = if(enable) ActiveTrackColor else InactiveTrackColor,
        textAlign = TextAlign.End
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showBackground = false)
fun EqualizerBandsPreview() {
    EqualizerSlider(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
        viewModel = FakePlayerViewModel()
    )
}