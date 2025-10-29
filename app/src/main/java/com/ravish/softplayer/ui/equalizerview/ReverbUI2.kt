package com.ravish.softplayer.ui.equalizerview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.AddIcon2
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.theme.TrackTitleTextColor
import com.ravish.softplayer.ui.theme.Typography
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun ReverbUI2(viewModel: PlayerViewModel, modifier: Modifier) {
    // 1. FIX: Added Environmental Reverb UI
    var reverbExpanded by remember { mutableStateOf(false) }
    val reverbPresets = viewModel.getReverbPresetNames()
    val currentReverb by viewModel.equalizerUIState.currentReverb.collectAsStateWithLifecycle()
    val isEnabled by viewModel.equalizerUIState.enableEqualizerState.collectAsStateWithLifecycle()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Reverb",
            modifier = Modifier.weight(1f),
            style = Typography.labelLarge,
            color = if (isEnabled) Color.Black else Color.LightGray
        )
        // Reverb Preset Spinner
        TextButton(onClick = { reverbExpanded = true }) {
            reverbPresets?.getOrElse(currentReverb) { "None" }?.let {
                Text(
                    text = it,
                    style = Typography.labelMedium,
                    color = if (isEnabled) Color.Black else Color.LightGray
                )
            }
            AddIcon2(
                modifier = modifier, R.drawable.icon_down_arrow,
                onClick = {

                },
                isEnabled = isEnabled
            )
        }
        DropdownMenu(
            expanded = reverbExpanded,
            onDismissRequest = { reverbExpanded = false }
        ) {
            reverbPresets?.forEachIndexed { index, name ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = name,
                            style = Typography.labelMedium,
                            color = TrackTitleTextColor
                        )
                    },
                    onClick = {
                        viewModel.setCurrentReverb(index)
                        reverbExpanded = false
                    }
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ReverbUIPreview() {
    ReverbUI2(viewModel= FakePlayerViewModel(), modifier = Modifier.fillMaxWidth())
}