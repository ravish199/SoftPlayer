package com.ravish.softplayer.ui.equalizerview

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.AddIcon
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun PresetUI(modifier: Modifier, viewModel: PlayerViewModel) {
    var presetName by remember { mutableStateOf("New") }
    var presetExpanded by remember { mutableStateOf(false) }
    val presetList = viewModel.getPresetData()
    if(presetList.isNotEmpty()) {
        viewModel.updatePresetBands(presetList[0].bandLevels)
    }
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (presetLabel, preset, button, dropdown) = createRefs()
        Text(
            modifier = modifier.constrainAs(presetLabel) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }, text = "Presets",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )

        Text(
            modifier = modifier
                .constrainAs(preset) {
                    start.linkTo(presetLabel.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(start = 20.dp), text = presetName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )

        AddIcon(modifier = modifier
            .constrainAs(button) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            .clickable {
              presetExpanded = !presetExpanded
            }, icon = if(presetExpanded) R.drawable.icon_up_arrow else R.drawable.icon_down_arrow)

        DropdownMenu(
            modifier = modifier.constrainAs(dropdown) {
                start.linkTo(presetLabel.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            expanded = presetExpanded,
            onDismissRequest = { presetExpanded = false }) {
            presetList.forEach {
                Log.d("PresetUI:", "Preset: ${it.name}")
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        presetName = it.name
                        presetExpanded = false
                        viewModel.updatePresetBands(it.bandLevels)
                    })
            }
        }

    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun DrawPresetUIPreview() {
    PresetUI(
        modifier = Modifier.background(color = Color.White),
        viewModel = FakePlayerViewModel()
    )
}