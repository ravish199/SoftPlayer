package com.ravish.softplayer.data.model.uistate

import kotlinx.coroutines.flow.StateFlow

data class EqualizerUIState(
    val openEqualizerState: StateFlow<Boolean>,
    val enableEqualizerState: StateFlow<Boolean>,
    val updatePresetBand: StateFlow<List<Float>>,
    val presetName: StateFlow<String>,
    val enableReverbState: StateFlow<Boolean>,
    val currentReverb: StateFlow<Int>,
    val updateReverb: StateFlow<Int>
    )