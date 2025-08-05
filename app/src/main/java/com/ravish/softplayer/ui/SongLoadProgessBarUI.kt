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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravish.softplayer.ui.theme.GlowColor
import kotlinx.coroutines.flow.StateFlow


var progressValue= mutableFloatStateOf(0f)
var sCount = 0
@Composable
fun DrawSongLoadProgress(modifier: Modifier) {
    val progressState by progressValue
    val progressText = "${(progressState * 100).toInt()}%"
    Box(modifier = modifier) {
        Text(modifier = Modifier.fillMaxWidth()
        .align(alignment = Alignment.Center)
            .padding(bottom = 20.dp, start = 50.dp, end = 50.dp),
            text = progressText,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = true,
            style = TextStyle(
            fontSize = 30.sp,

            color = Color.White,
                shadow = Shadow(
                    color = Color.Black,
                    blurRadius = 20f
                )
        ))
        CircularProgressIndicator(
            progress = { progressState },
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f).align(alignment = Alignment.Center),
            color = GlowColor,
            strokeWidth = 20.dp,
            trackColor = Color.White,
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