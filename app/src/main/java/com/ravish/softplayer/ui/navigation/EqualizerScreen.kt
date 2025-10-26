package com.ravish.softplayer.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ravish.softplayer.ui.equalizerview.EqualizerUI
import com.ravish.softplayer.ui.theme.dialogBackground
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun EqualizerScreen(viewModel: PlayerViewModel) {
    viewModel.initializeEqualizer()
    EqualizerUI(
        modifier = Modifier
            .fillMaxSize()
            .background(dialogBackground), viewModel = viewModel
    )
}