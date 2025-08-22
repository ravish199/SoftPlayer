package com.ravish.softplayer.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravish.softplayer.R
import com.ravish.softplayer.data.model.EquilizerSelection
import com.ravish.softplayer.ui.theme.ButtonBackgroundColor
import com.ravish.softplayer.ui.theme.ButtonTextColor
import com.ravish.softplayer.ui.theme.HighLightColor
import com.ravish.softplayer.ui.theme.NormalStateColor

@Composable
fun DrawEquilizerUI(modifier: Modifier) {
    var eqState by remember { mutableStateOf(EquilizerSelection.SOFT) }
            Row(
                modifier = modifier.background(ButtonBackgroundColor),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AddIconWithLabel(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            eqState = EquilizerSelection.SOFT
                        },
                    selected = eqState == EquilizerSelection.SOFT,
                    icon = R.drawable.frequncry_icon,
                    label = "Soft"
                )
                AddIconWithLabel(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            eqState = EquilizerSelection.ROCK
                        },
                    selected = eqState == EquilizerSelection.ROCK,
                    icon = R.drawable.frequncry_icon,
                    label = "Rock"
                )
                AddIconWithLabel(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            eqState = EquilizerSelection.BEAT
                        },
                    selected = eqState == EquilizerSelection.BEAT,
                    icon = R.drawable.frequncry_icon,
                    label = "Beat"
                )
                AddIconWithLabel(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            eqState = EquilizerSelection.CLASSIC
                        },
                    selected = eqState == EquilizerSelection.CLASSIC,
                    icon = R.drawable.frequncry_icon,
                    label = "Classic"
                )
                AddIconWithLabel(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            eqState = EquilizerSelection.EXTRA_BEAT
                        },
                    selected = eqState == EquilizerSelection.EXTRA_BEAT,
                    icon = R.drawable.frequncry_icon,
                    label = "Extra Beat"
                )

    }
}

@Composable
fun AddIconWithLabel(modifier: Modifier, icon: Int, label: String, selected: Boolean = false) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AddIcon(modifier = modifier.align(Alignment.CenterHorizontally), icon, selected = selected)
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = label,
            color = if (selected) Color.White else Color.LightGray,
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
