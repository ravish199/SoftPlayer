package com.ravish.softplayer.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.theme.SongsTileBackgroundColor

@Composable
fun DrawEquilizerUI(modifier: Modifier) {
    Row(
        modifier = modifier.background(SongsTileBackgroundColor),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AddIconWithLabel(modifier = Modifier.wrapContentSize().weight(1f), R.drawable.frequncry_icon, "Eq1")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().weight(1f), R.drawable.frequncry_icon, "Eq2")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().weight(1f), R.drawable.frequncry_icon, "Eq3")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().weight(1f), R.drawable.frequncry_icon, "Eq4")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().weight(1f), R.drawable.frequncry_icon, "Eq5")
    }
}

@Composable
fun AddIconWithLabel(modifier: Modifier, icon: Int, label: String) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        AddIcon(modifier = modifier.align(Alignment.CenterHorizontally), icon)
        Text(modifier = modifier.align(Alignment.CenterHorizontally),text = label, color = Color.White ,textAlign = TextAlign.Center)
    }
}


@Composable
@Preview
fun DrawEquilizerUIPreview() {
    DrawEquilizerUI(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
    )
}
