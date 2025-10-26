package com.ravish.softplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
        IconButton(
            modifier = modifier,
            onClick = { /*TODO*/ },
        ) {
            Icon(
                modifier = modifier.size(100.dp).padding(10.dp),
                contentDescription = "Previous",
                painter = painterResource(id = icon),
                tint = if (selected) ButtonContainerColor else NormalStateColor
            )
        }

}

@Composable
fun AddIcon1(modifier: Modifier, icon: Int) {
    IconButton(
        modifier = modifier,
        onClick = { /*TODO*/ },
    ) {
        Icon(
            modifier = modifier.size(100.dp).padding(10.dp),
            contentDescription = "Previous",
            painter = painterResource(id = icon),
        )
    }

}

@Composable
fun AddIcon2(modifier: Modifier, icon: Int, onClick: ()-> Unit, isEnabled: Boolean = true) {
    Box(modifier = modifier.background(
        color = Color.Transparent,
        shape = CircleShape,
    )) {
        IconButton(
            modifier = modifier,
            onClick = onClick,
            enabled = isEnabled
        ) {
            Icon(
                modifier = modifier.padding(1.dp),
                contentDescription = "Previous",
                painter = painterResource(id = icon),
                tint = if(isEnabled) ButtonContainerColor else NormalStateColor
            )
        }
    }
}


@Preview(showBackground = false)
@Composable
fun AddIconPreview() {
    AddIcon(
        modifier = Modifier.wrapContentSize().background(color =Color.Transparent),
        R.drawable.icon_next_new2)
}