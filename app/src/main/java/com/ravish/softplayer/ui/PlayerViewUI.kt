package com.ravish.softplayer.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ravish.softplayer.ui.sliderview.SliderView
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

val thumbImage = mutableStateOf<Bitmap?>(null)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawPayerView(viewModel: PlayerViewModel, modifier: Modifier) {

    Log.d("DrawPayerView", "DrawPayerView")


    Column(
        modifier = modifier
    ) {

        DrawSongInfo(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        )

        SliderView(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )


    }
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
