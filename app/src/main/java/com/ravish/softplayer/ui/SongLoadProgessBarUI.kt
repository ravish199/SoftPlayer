package com.ravish.softplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravish.softplayer.ui.theme.HighLightColor

import kotlinx.coroutines.flow.StateFlow


var progressValue= mutableFloatStateOf(0f)
var sCount = 0
@Composable
fun DrawSongLoadProgress(modifier: Modifier, strokeWidth: Dp = 10.dp) {
    val progressState by progressValue
    val progressText = "Loading media ${(progressState * 100).toInt()}%"
    Box(modifier = modifier) {
        Text(modifier = Modifier.fillMaxWidth()
        .align(alignment = Alignment.Center)
            .padding(bottom = 20.dp, start = 50.dp, end = 50.dp, top = 60.dp),
            text = progressText,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = true,
            style = TextStyle(
            fontSize = 20.sp,

            color = Color.Cyan,
                shadow = Shadow(
                    color = Color.Red,
                    blurRadius = 20f
                )
        ))
        LinearProgressIndicator(
            progress = { progressState },
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.02f).align(alignment = Alignment.Center),
            color = Color.Cyan,
            trackColor = Color.Transparent,
            strokeCap = StrokeCap.Round,
        )
    }
}

fun setProgress(progress: Float, songCount: Int){
    sCount = songCount
    progressValue.value = progress
}

@Composable
@Preview
fun DrawSongLoadProgressPreview() {
    DrawSongLoadProgress(modifier = Modifier.fillMaxSize())

}