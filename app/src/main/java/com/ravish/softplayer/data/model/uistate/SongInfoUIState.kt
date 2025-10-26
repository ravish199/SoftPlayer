package com.ravish.softplayer.data.model.uistate

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.StateFlow

data class SongInfoUIState(
    val songTitleState:StateFlow<String>,
    val albumState:StateFlow<String>,
    val artistsState:StateFlow<String>,
    val imageUriState:StateFlow<Uri?>,
    val playerBackgroundState:StateFlow<Bitmap?>,
)