package com.ravish.softplayer.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravish.softplayer.R
import com.ravish.softplayer.Screen
import com.ravish.softplayer.firstBaselineToTop


@Composable
fun SongLoadingScreen() {
    Box(
        modifier = Modifier.fillMaxHeight().fillMaxWidth().background(Color.Black),
    contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize().padding().background(Color.Black),
            painter = painterResource(id = R.drawable.music_image_v),
            contentScale = ContentScale.FillHeight,
            contentDescription = ""
        )

        Box(modifier = Modifier.wrapContentSize().padding(top = 15.dp),
            contentAlignment = Alignment.CenterStart) {
            DrawSongLoadProgress(
                modifier = Modifier.fillMaxWidth(), strokeWidth = 15.dp
            )
        }


    }
}

@Composable
@Preview
fun SongLoadingScreenPreview() {
    SongLoadingScreen()
}