package com.ravish.softplayer.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravish.softplayer.ui.theme.dialogBackground
import com.ravish.softplayer.ui.trackui.TrackUI
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun TrackListScreen(viewModel: PlayerViewModel) {
    val audioList by viewModel.mediaUpdateUIState.mediaItemsUpdateState.collectAsStateWithLifecycle()
    Box(modifier = Modifier.fillMaxSize()) {
        TrackUI(viewModel = viewModel,
            modifier = Modifier.fillMaxSize().background(dialogBackground)
            , audioList = audioList)
    }
}