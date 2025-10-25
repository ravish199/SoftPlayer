package com.ravish.softplayer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OverLayView(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val brush =  Brush.verticalGradient(
            colors = listOf(Color.Transparent,
                Color.Transparent,
                Color.Transparent,
                Color.White,
                Color.White,
                Color.White),
            tileMode= TileMode.Repeated)
        drawRect(
            brush =brush,
            size = size,
            style = Fill
        )
    }
}

@Composable
@Preview(showBackground = false)
fun OverLayViewPreview() {
    OverLayView(modifier = Modifier.fillMaxSize())

}