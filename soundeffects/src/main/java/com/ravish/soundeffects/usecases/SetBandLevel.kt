package com.ravish.soundeffects.usecases

import com.ravish.soundeffects.AudioEffectManager

class SetBandLevel(private val audioEffectManager: AudioEffectManager) {
    operator fun invoke(band: Short, bandLevel: Float) {
        audioEffectManager.setBandLevel(band, bandLevel)
    }
}