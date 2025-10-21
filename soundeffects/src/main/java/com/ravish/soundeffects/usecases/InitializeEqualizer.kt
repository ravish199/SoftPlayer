package com.ravish.soundeffects.usecases

import com.ravish.soundeffects.AudioEffectManager

class InitializeEqualizer(private val audioEffectManager: AudioEffectManager) {
    operator fun invoke(sessionId: Int?) {
        audioEffectManager.initEqualizer(sessionId)
    }
}