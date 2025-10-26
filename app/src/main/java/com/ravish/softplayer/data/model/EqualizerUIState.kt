package com.ravish.softplayer.data.model

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class EqualizerUIState(
    val openEqualizerState: StateFlow<Boolean>,
    val enableEqualizerState: StateFlow<Boolean>,
    val updatePresetBand: StateFlow<List<Float>>,
    val presetName: StateFlow<String>,
    )