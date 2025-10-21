package com.ravish.soundeffects.usecases

import com.ravish.soundeffects.AudioEffectManager

class EnableEqualizer(private val audioEffectManager: AudioEffectManager) {
    operator fun invoke(enable: Boolean) {
        audioEffectManager.enableEqualizer(enable)
    }
}