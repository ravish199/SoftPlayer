package com.ravish.softplayer.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.ui.theme.ButtonBackgroundColor

@Composable
fun AddIcon(modifier: Modifier, icon: Int) {
    IconButton(
        modifier = modifier.padding(10.dp),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = Color.White,
            containerColor = ButtonBackgroundColor
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