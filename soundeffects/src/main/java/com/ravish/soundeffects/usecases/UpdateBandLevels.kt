package com.ravish.soundeffects.usecases

import com.ravish.soundeffects.AudioEffectManager
import kotlin.Float

class UpdateBandLevels(private val audioEffectManager: AudioEffectManager) {
    operator fun invoke(levels: Array<Float>) {
        audioEffectManager.updateBandLevels(levels)
    }
}