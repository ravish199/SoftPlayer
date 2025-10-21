package com.ravish.softplayer.ui.equalizerview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ravish.softplayer.ui.theme.dialogBackground
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun DrawEqualizerDialog(modifier: Modifier, viewModel: PlayerViewModel) {

    viewModel.initializeEqualizer()
   Box(modifier = modifier) {
           Dialog(onDismissRequest = { viewModel.closeEqualizer() },
           ) {
               EqualizerScreen(modifier = Modifier.fillMaxHeight(0.8f)
                   .fillMaxWidth().align(alignment = Alignment.BottomStart)
                   .background(dialogBackground)
                  , viewModel = viewModel)
       }
   }
}

@Composable
@Preview
fun DrawEqualizerPreview() {
    DrawEqualizerDialog(modifier = Modifier.fillMaxSize(), viewModel = viewModel())
}