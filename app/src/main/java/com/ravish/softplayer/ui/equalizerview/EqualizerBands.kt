package com.ravish.softplayer.ui.equalizerview

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.sliderview.SliderView2
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerBands(modifier: Modifier, viewModel: PlayerViewModel) {
    Column(
        modifier = modifier
    ) {


        Column(
            modifier = modifier
                .fillMaxSize()
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            ) {

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // 'size' provides the width and height of the Canvas
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Calculate the Y position for the center line
                    val centerY = canvasHeight / 2

                    // Draw a horizontal line from the left edge to the right edge at the center
                    drawLine(
                        color = Color.Gray, // Use a color that fits your theme
                        start = Offset(x = 0f, y = centerY),
                        end = Offset(x = canvasWidth, y = centerY),
                        strokeWidth = 2.dp.toPx(), // Make the line 2dp thick
                    )
                }

                EqualizerSlider(
                    modifier = Modifier.fillMaxSize(),
                    noOfSlider = 5,
                    viewModel = viewModel
                )


            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            addLabelText(R.string.band1_label)
            addLabelText(R.string.band2_label)
            addLabelText(R.string.band3_label)
            addLabelText(R.string.band4_label)
            addLabelText(R.string.band5_label)
        }
    }
}

@Composable
fun addLabelText(labelResId: Int) {
    Text(
        text = LocalContext.current.getString(labelResId),
        color = Color.Gray,
    )
}


@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerSlider(
    modifier: Modifier,
    noOfSlider: Int,
    viewModel: PlayerViewModel,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationZ = -90f
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0.5f)
            },
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Use a Box to contain the rotated slider
        val eqBandsLevel by viewModel.updatePresetBand.collectAsStateWithLifecycle()


        for (i in 0..noOfSlider - 1) {
            val sliderValues = remember { mutableFloatStateOf(eqBandsLevel[i]) }
            val interactionSource = remember { MutableInteractionSource() }
            // var sliderValue by remember { mutableFloatStateOf(0f) }
            var isDragging by remember { mutableStateOf(false) }
            val sliderMaxValue = 100f
            SliderView2(
                modifier = Modifier.fillMaxWidth(),
                value = sliderValues.floatValue,
                onValueChange = { newValue ->
                    isDragging = true
                    sliderValues.floatValue = newValue
                },
                onValueChangeFinished = {
                    isDragging = false

                    viewModel.setBandLevel(i, sliderValues.floatValue)
                    Log.d("VerticalSlider", "slider value $i: ${sliderValues.floatValue}")
                },
                0f..1f,
                true,
                interactionSource
            )

            LaunchedEffect(eqBandsLevel) {
                sliderValues.floatValue = eqBandsLevel[i]
            }
        }

    }
}


/*@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun EqualizerSliderPreview() {
    val m = Modifier
        .fillMaxSize()
        .background(color = Color.White)
        .height(0.dp)
    Row(
        modifier = m, // Give the container a fixed height for preview
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EqualizerSlider(
            modifier = Modifier.fillMaxWidth(0.7f)
                .fillMaxHeight(0.3f)
                .background(color = Color.Green),
            noOfSlider = 5,
            viewModel = FakePlayerViewModel()
        )
    }
}*/

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun EqualizerBandsPreview() {
    EqualizerBands(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.7f),
        viewModel = FakePlayerViewModel()
    )
}