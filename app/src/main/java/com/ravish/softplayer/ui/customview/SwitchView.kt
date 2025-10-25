package com.ravish.softplayer.ui.customview

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import com.ravish.softplayer.ui.theme.ButtonContainerColor
import com.ravish.softplayer.ui.theme.ButtonContainerColorSemiTransparent

@Composable
fun DrawSwitch(modifier: Modifier) {
    var enabled by remember { mutableStateOf(true) }
    Switch(
        modifier = modifier.scale(0.5f),
        checked = enabled,
        onCheckedChange = { enabled = it },
        colors = SwitchDefaults.colors(
            checkedThumbColor = ButtonContainerColor,
            checkedTrackColor = ButtonContainerColorSemiTransparent
        )
    )
}

@Composable
@Preview(showBackground = false)
fun DrawSwitchPreview() {
    DrawSwitch(modifier = Modifier)
}