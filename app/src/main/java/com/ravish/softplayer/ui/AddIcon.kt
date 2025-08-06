package com.ravish.softplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.theme.ButtonContainerColor
import com.ravish.softplayer.ui.theme.HighLightColor
import com.ravish.softplayer.ui.theme.NormalStateColor
import com.ravish.softplayer.ui.theme.NormalStateColorContainer

@Composable
fun AddIcon(modifier: Modifier, icon: Int, selected: Boolean = false) {
    Box(modifier = modifier.padding(10.dp).background(ButtonContainerColor)) {
        IconButton(
            modifier = modifier.padding(),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = if (selected) HighLightColor else NormalStateColor
            ),
            onClick = { /*TODO*/ },
        ) {
            Icon(
                modifier = modifier.fillMaxSize(),
                contentDescription = "Previous",
                painter = painterResource(id = icon),
            )
        }
    }
}


@Preview
@Composable
fun AddIconPreview() {
    AddIcon(modifier = Modifier.wrapContentSize(), R.drawable.play_icon)
}