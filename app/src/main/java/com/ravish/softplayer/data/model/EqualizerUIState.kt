package com.ravish.softplayer.data.model

import android.graphics.Bitmap
import kotlinx.coroutines.flow.StateFlow

data class EqualizerUIState(
    val numberOfBands:Short,
    val bandLevelRange:Pair<Short, Short>,
    val centerFreq:Int

)