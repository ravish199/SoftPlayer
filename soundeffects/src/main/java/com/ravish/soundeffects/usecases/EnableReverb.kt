package com.ravish.soundeffects.usecases

import com.ravish.soundeffects.AudioEffectManager

class EnableReverb(private val audioEffectManager: AudioEffectManager) {
    operator fun invoke(enable: Boolean) {
        audioEffectManager.enableReverb(enable)
    }
}