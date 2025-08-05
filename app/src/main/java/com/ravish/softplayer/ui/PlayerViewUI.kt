package com.ravish.softplayer.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.mutableFloatSetOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.theme.AppBackgroundColor
import com.ravish.softplayer.ui.theme.PlayerControlBackgroundColor

val thumbImage = mutableStateOf<Bitmap?>(null)
@Composable
fun DrawPayerView(modifier: Modifier) {
    Column(
        modifier = modifier.background(Color.Transparent),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        var sliderPosition by remember { mutableFloatStateOf(50f) }
        Box() {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                bitmap = (thumbImage.value ?: BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    R.drawable.app_background
                )).asImageBitmap(),
                contentDescription = "Song Name",
                contentScale = ContentScale.FillBounds
            )
        }

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
        thumbColor = Color.Red,
                activeTrackColor = Color.Cyan,
                inactiveTrackColor = Color.Red
            )
        )

    }
}

fun updateThumbImage(bitmap: Bitmap) {
    thumbImage.value = bitmap
    updateBackground(bitmap)
}


@Preview
@Composable
fun DrawPayerViewPreview() {
    DrawPayerView(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
}
