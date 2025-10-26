package com.ravish.softplayer.data.model

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class TrackListUIState(
    val openTrackListStatus: StateFlow<Boolean>,
    val filterTrack: StateFlow<String>
    )