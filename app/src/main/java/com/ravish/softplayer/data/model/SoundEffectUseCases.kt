package com.ravish.softplayer.data.model

import com.ravish.soundeffects.usecases.UpdateBandLevels
import com.ravish.soundeffects.usecases.EnableEqualizer
import com.ravish.soundeffects.usecases.EnableReverb
import com.ravish.soundeffects.usecases.InitializeEqualizer
import com.ravish.soundeffects.usecases.SetBandLevel

data class SoundEffectUseCases(
    val initializeEqualizer: InitializeEqualizer,
    val enableEqualizer: EnableEqualizer,
    val enableReverb: EnableReverb,
    val updateBandLevel: UpdateBandLevels,
    val setBandLevel: SetBandLevel
)
