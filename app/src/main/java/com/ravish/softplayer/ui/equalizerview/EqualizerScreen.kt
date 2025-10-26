package com.ravish.softplayer.ui.equalizerview

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.AddIcon2
import com.ravish.softplayer.ui.theme.ButtonContainerColor
import com.ravish.softplayer.ui.theme.ButtonContainerColorSemiTransparent
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun EqualizerScreen(modifier: Modifier, viewModel: PlayerViewModel) {
    val primary = Color(0xFFFF6A00)
    val isEnabled by viewModel.equalizerUIState!!.enableEqualizerState.collectAsStateWithLifecycle()
    var presetExpanded by remember { mutableStateOf(false) }
    var presetName by remember { mutableStateOf("New") }
    var preamp by remember { mutableStateOf(0f) }
    var snapBands by remember { mutableStateOf(true) }
    val sizeRatio = 0.5f

    Column(
        modifier = modifier.padding(top = 30.dp, start = 8.dp, end = 8.dp)
    ) {

        // Top row: title + enable switch
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()

        ) {
            val (eqLabel, enable, enableSwitch) = createRefs()

            AddIcon2(
                modifier = Modifier
                    .constrainAs(eqLabel) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .paddingFromBaseline( bottom = 8.dp),
                onClick = {
                    viewModel.closeEqualizer()
                },
                icon = R.drawable.icon_back_arrow
            )

            Text(
                modifier = Modifier
                    .constrainAs(enable) {
                        end.linkTo(enableSwitch.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(end = 20.dp),
                text = "Enable", fontSize = (28 * sizeRatio).sp, fontWeight = FontWeight.Normal
            )
            Switch(
                modifier = Modifier
                    .constrainAs(enableSwitch) {
                        end.linkTo(parent.end)
                    }
                    .scale(0.6f),

                checked = isEnabled,
                onCheckedChange = {
                    Log.d("EqualizerScreen", "onCheckedChange: $isEnabled")
                    viewModel.updateAndSaveEqEnabled(!isEnabled)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ButtonContainerColor,
                    checkedTrackColor = ButtonContainerColorSemiTransparent
                )
            )
        }

        // Presets row
        PresetUI(modifier = Modifier, viewModel = viewModel)

        // Bands area

        // Snap bands toggle

        Row(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth(), // Give the container a fixed height for preview
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            EqualizerSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), viewModel = viewModel
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text("Snap bands", modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            Spacer(Modifier.width(8.dp))
            Switch(
                modifier = Modifier.scale(0.6f),
                checked = snapBands,
                onCheckedChange = { snapBands = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ButtonContainerColor,
                    checkedTrackColor = ButtonContainerColorSemiTransparent
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        // Buttons: Delete Reset Save
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            TextButton(onClick = { /*delete*/ }) {
                Text(
                    "DELETE",
                    color = primary,
                    fontWeight = FontWeight.Bold
                )
            }
            TextButton(onClick = { /*reset*/ }) {
                Text(
                    "RESET",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = { /*save*/ },
                colors = ButtonDefaults.buttonColors(containerColor = primary)
            ) { Text("SAVE", color = Color.White, fontWeight = FontWeight.Bold) }
        }
    }
}

// Preview helper (if using Android Studio preview)
@Composable
@Preview(showBackground = true)
fun EqualizerPreview() {

    EqualizerScreen(modifier = Modifier.fillMaxHeight(0.8f), viewModel = viewModel())
}
