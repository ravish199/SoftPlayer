package com.ravish.softplayer.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravish.softplayer.R
import com.ravish.softplayer.Screen


@Composable
fun SongLoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.padding(bottom = 18.dp).background(Color.Transparent),
            painter = painterResource(id = R.drawable.app_background),
            contentScale = ContentScale.FillBounds,
            contentDescription = ""
        )


        DrawSongLoadProgress(
            modifier = Modifier.fillMaxSize(0.5f)
        )


    }
}

@Composable
@Preview
fun SongLoadingScreenPreview() {
    SongLoadingScreen()
}