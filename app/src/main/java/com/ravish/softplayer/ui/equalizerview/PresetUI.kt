package com.ravish.softplayer.ui.equalizerview

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravish.softplayer.ui.AddIcon2
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.theme.ButtonBackgroundColor
import com.ravish.softplayer.ui.theme.ButtonContainerColor
import com.ravish.softplayer.ui.theme.TransparentColor
import com.ravish.softplayer.ui.theme.Typography
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel
import java.nio.file.WatchEvent

@Composable
fun PresetUI(modifier: Modifier, viewModel: PlayerViewModel) {

    var presetExpanded by remember { mutableStateOf(false) }
    val presetList = viewModel.getPresetData()
    /*  if(presetList.isNotEmpty()) {
          viewModel.updatePresetBands(presetName = presetList[0].name, levels = presetList[0].bandLevels)
      }*/
    val presetName = viewModel.presetName.collectAsStateWithLifecycle().value.ifEmpty { "Flat" }
    var selectedPresetName = remember { mutableStateOf(presetName) }
    val isEnabled by viewModel.enableEqualizerState.collectAsStateWithLifecycle()
    LaunchedEffect(presetName) {
        viewModel.updatePresetBands(
            presetName = presetName,
            levels = presetList[presetList.map { it.name }.indexOf(presetName)].bandLevels
        )
    }


    Box(modifier = modifier) {
        Row(
            modifier = Modifier.clickable {
                if (isEnabled) {
                    presetExpanded = !presetExpanded
                }
            },
            horizontalArrangement = Arrangement.Start
        ) {
            val textModifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(start = 8.dp, end = 8.dp)
            Text(
                modifier = textModifier.weight(1f),
                text = "Presets",
                style = Typography.labelLarge,
                color = if (isEnabled) Color.Black else Color.LightGray

            )

            Text(
                modifier = textModifier.weight(2f),
                text = selectedPresetName.value,
                style = Typography.labelLarge,
                color = if (isEnabled) Color.Black else Color.LightGray
            )

            AddIcon2(
                modifier = textModifier.weight(1f),
                icon = if (presetExpanded) com.ravish.softplayer.R.drawable.icon_up_arrow else com.ravish.softplayer.R.drawable.icon_down_arrow,
                onClick = {
                    presetExpanded = !presetExpanded
                },
                isEnabled = isEnabled
            )

        }
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(0.6f).fillMaxHeight(0.6f).align(alignment = Alignment.CenterStart),
            containerColor = Color.White,
            expanded = presetExpanded,
            tonalElevation = 10.dp,
            shadowElevation = 10.dp,
            onDismissRequest = { presetExpanded = false }) {
            presetList.forEach {
                Log.d("PresetUI:", "Preset: ${it.name}")
                DropdownMenuItem(
                    modifier = modifier.fillMaxWidth(0.6f).
                    background(color=Color.White).align(alignment = Alignment.CenterHorizontally),
                    colors = MenuDefaults.itemColors(textColor = if(it.name == presetName) ButtonContainerColor else Color.Black),
                    text = {
                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            text = it.name
                        )
                    },
                    onClick = {
                        selectedPresetName.value = it.name
                        presetExpanded = false
                        viewModel.updatePresetBands(it.name, it.bandLevels)
                    })
            }
        }
    }

}


/*
@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showBackground = false)
fun DrawPresetUIPreview() {
    PresetUI(
        modifier = Modifier.background(color = Color.White),
        viewModel = FakePlayerViewModel()
    )
}*/
