package com.ravish.softplayer.ui.equalizerview

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.AddIcon
import com.ravish.softplayer.ui.AddIcon2
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.theme.ButtonContainerColor
import com.ravish.softplayer.ui.theme.ButtonContainerColorSemiTransparent
import com.ravish.softplayer.ui.theme.Typography
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun ReverbUI(modifier: Modifier, viewModel: PlayerViewModel) {
    viewModel.getPreset()



    var reverbExpanded by remember { mutableStateOf(false) }
    val reverbPresets = viewModel.getReverbPresetNames()
    val currentReverb by viewModel.equalizerUIState.currentReverb.collectAsStateWithLifecycle()
    val isEnabled by viewModel.equalizerUIState.enableReverbState.collectAsStateWithLifecycle()
    val updateReverb by viewModel.equalizerUIState.updateReverb.collectAsStateWithLifecycle()
    val reverbName by remember { mutableStateOf(reverbPresets?.get(currentReverb) ?: "None") }
    LaunchedEffect(currentReverb) {
        Log.d("Reverb", "Reverb: $currentReverb")
        viewModel.updateReverb(
            reverbIndex = currentReverb
        )
    }


    Box(modifier = modifier) {
        Row(
            modifier = Modifier.clickable {
                if (isEnabled) {
                    reverbExpanded = !reverbExpanded
                }
            },
            horizontalArrangement = Arrangement.Start
        ) {
            val textModifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(start = 8.dp, end = 8.dp)
            Text(
                modifier = textModifier.weight(1f),
                text = "Reverb",
                style = Typography.labelLarge,
                color = if (isEnabled) Color.Black else Color.LightGray

            )

            Text(
                modifier = textModifier.weight(2f),
                text = reverbPresets?.get(currentReverb) ?: "None",
                style = Typography.labelLarge,
                color = if (isEnabled) Color.Black else Color.LightGray
            )

            AddIcon2(
                modifier = textModifier.weight(1f),
                icon = if (reverbExpanded) R.drawable.icon_up_arrow else R.drawable.icon_down_arrow,
                onClick = {
                    reverbExpanded = !reverbExpanded
                },
                isEnabled = isEnabled
            )


            Switch(
                modifier = Modifier.weight(1f)
                    .scale(0.6f),

                checked = isEnabled,
                onCheckedChange = {
                    Log.d("EqualizerScreen", "onCheckedChange: $isEnabled")
                    viewModel.enableReverb(!isEnabled)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ButtonContainerColor,
                    checkedTrackColor = ButtonContainerColorSemiTransparent
                )
            )

        }
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(0.6f).fillMaxHeight(0.6f).align(alignment = Alignment.CenterStart),
            containerColor = Color.White,
            expanded = reverbExpanded,
            tonalElevation = 10.dp,
            shadowElevation = 10.dp,
            onDismissRequest = { reverbExpanded = false }) {
            reverbPresets?.forEach {
                Log.d("PresetUI:", "Preset: ${it}")
                DropdownMenuItem(
                    modifier = modifier.fillMaxWidth(0.6f).
                    background(color=Color.White).align(alignment = Alignment.CenterHorizontally),
                    colors = MenuDefaults.itemColors(textColor = if(it == reverbPresets[currentReverb])
                        ButtonContainerColor else Color.Black),
                    text = {
                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = it
                        )
                    },
                    onClick = {
                        reverbExpanded = false
                        viewModel.setCurrentReverb(reverbPresets.indexOf(it))
                    })
            }
        }


    }

}


@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showBackground = false)
fun DrawReverbUIPreview() {
    ReverbUI(
        modifier = Modifier.background(color = Color.White),
        viewModel = FakePlayerViewModel()
    )
}