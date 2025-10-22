package com.ravish.softplayer.ui.equalizerview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreampUI(modifier: Modifier) {
   Row(verticalAlignment = Alignment.CenterVertically) {
       Text(modifier = Modifier.padding(end = 20.dp),
           text = "Preamp",   fontSize = 12.sp,
           fontWeight = FontWeight.Normal)
        PreampSlider(modifier = modifier)
   }
}


@Composable
@Preview
fun PreampUIPreview() {
  PreampUI(modifier = Modifier.fillMaxWidth().background(color = Color.White))
}