package com.ravish.softplayer.data.model

import android.graphics.Bitmap
import kotlinx.coroutines.flow.StateFlow

data class SongInfoUIState(
    val songTitleState:StateFlow<String>,
    val artistsState:StateFlow<String>,
    val playerBackgroundState:StateFlow<Bitmap?>,
)