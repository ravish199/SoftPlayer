package com.ravish.soundeffects

import com.ravish.soundeffects.data.EqualizerPreset
import com.ravish.soundeffects.usecases.UpdateBandLevels
import kotlinx.coroutines.flow.StateFlow

interface AudioEffectManager {

    fun initEqualizer(sessionId: Int?)

    fun enableEqualizer(enable: Boolean)

    fun setBandLevel(band: Short, bandLevel: Float)

    fun getBandLevels(): Array<Float>?

    fun updateBandLevels(levels: Array<Float>)

    fun getPresetData():List<EqualizerPreset>
}