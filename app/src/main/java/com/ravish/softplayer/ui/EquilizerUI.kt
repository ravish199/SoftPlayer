package com.ravish.softplayer.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ravish.softplayer.R
import com.ravish.softplayer.data.model.EquilizerSelection
import com.ravish.softplayer.ui.theme.GlowColor
import com.ravish.softplayer.ui.theme.HighLightColor
import com.ravish.softplayer.ui.theme.SongsTileBackgroundColor

@Composable
fun DrawEquilizerUI(modifier: Modifier) {
    var eqState by remember { mutableStateOf(EquilizerSelection.EQ1) }
    Row(
        modifier = modifier.background(Color.Transparent),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AddIconWithLabel(modifier = Modifier.wrapContentSize().clickable {
            eqState = EquilizerSelection.EQ1
        },
            selected = eqState == EquilizerSelection.EQ1, icon =  R.drawable.icon_equilizer, label = "Eq1")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().clickable {
            eqState = EquilizerSelection.EQ2
        },
            selected = eqState == EquilizerSelection.EQ2, icon = R.drawable.icon_equilizer, label = "Eq2")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().clickable {
            eqState = EquilizerSelection.EQ3
        },
            selected = eqState ==  EquilizerSelection.EQ3, icon = R.drawable.icon_equilizer, label = "Eq3")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().clickable {
            eqState = EquilizerSelection.EQ4
        },
            selected = eqState ==  EquilizerSelection.EQ4, icon = R.drawable.icon_equilizer, label = "Eq4")
        AddIconWithLabel(modifier = Modifier.wrapContentSize().clickable {
            eqState = EquilizerSelection.EQ5
        },
            selected = eqState ==  EquilizerSelection.EQ5, icon = R.drawable.icon_equilizer, label = "Eq5")
    }
}

@Composable
fun AddIconWithLabel(modifier: Modifier, icon: Int, label: String, selected: Boolean = false) {
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        AddIcon(modifier = modifier.align(Alignment.CenterHorizontally), icon, selected = selected)
        Text(modifier = modifier.align(Alignment.CenterHorizontally),
            text = label,
            color = if(selected) HighLightColor else Color.White ,
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = TextStyle(
                fontSize = 10.sp,

            )
        )
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
