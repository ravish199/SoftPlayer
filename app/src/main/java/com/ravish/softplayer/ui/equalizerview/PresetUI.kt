package com.ravish.softplayer.ui.equalizerview

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.ravish.softplayer.ui.AddIcon
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.theme.Typography
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun PresetUI(modifier: Modifier, viewModel: PlayerViewModel) {

    var presetExpanded by remember { mutableStateOf(false) }
    val presetList = viewModel.getPresetData()
    /*  if(presetList.isNotEmpty()) {
          viewModel.updatePresetBands(presetName = presetList[0].name, levels = presetList[0].bandLevels)
      }*/
    val presetName = viewModel.presetName.collectAsStateWithLifecycle().value.ifEmpty { "Flat" }
    var selectedPresetName = remember { mutableStateOf(presetName) }
    LaunchedEffect(presetName) {
        viewModel.updatePresetBands(
            presetName = presetName,
            levels = presetList[presetList.map { it.name }.indexOf(presetName)].bandLevels
        )
    }


    Box(modifier = modifier) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Start
        ) {
            val textModifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(start = 8.dp, end = 8.dp)
            Text(
                modifier = textModifier.weight(1f),
                text = "Presets",
                style = Typography.labelMedium
            )

            Text(
                modifier = textModifier.weight(2f),
                  text = selectedPresetName.value,
                style = Typography.labelSmall
            )

            AddIcon(modifier = textModifier.weight(1f)
                .clickable {
                    presetExpanded = !presetExpanded
                }, icon = if(presetExpanded) com.ravish.softplayer.R.drawable.icon_up_arrow else com.ravish.softplayer.R.drawable.icon_down_arrow)

        }
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(0.5f)
                .align(alignment = Alignment.Center),
            expanded = presetExpanded,
            onDismissRequest = { presetExpanded = false }) {
            presetList.forEach {
                Log.d("PresetUI:", "Preset: ${it.name}")
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        selectedPresetName.value = it.name
                        presetExpanded = false
                        viewModel.updatePresetBands(it.name, it.bandLevels)
                    })
            }
        }
    }

}


@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showBackground = false)
fun DrawPresetUIPreview() {
    PresetUI(
        modifier = Modifier.background(color = Color.White),
        viewModel = FakePlayerViewModel()
    )
}