package com.ravish.softplayer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawSongHeader(modifier: Modifier, categoryName: String?=null, songCount: Int?=null) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier.padding(
            5.dp
        ),
            text = categoryName?:"",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(modifier = Modifier.padding(
            5.dp
        ),
            text = songCount?.toString()?:"0",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DrawSongInfo(modifier: Modifier, title: String? = null, singerName: String? = null) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(
                10.dp
            ).fillMaxWidth(), text = title?:"",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(
                10.dp
            ).fillMaxWidth(),
            text = singerName?:"",
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun DrawSongHeaderPreview() {
    Column {
        DrawSongHeader(modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth())
        DrawSongInfo(modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth())
    }

}