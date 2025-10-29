package com.ravish.soundeffects.data

internal object ReverbRepository {

     val getReverbPresetNames = listOf(
        "None",
        "Small Room",
        "Medium Room",
        "Large Room",
        "Medium Hall",
        "Large Hall",
        "Plate"
        // You can add more presets from EnvironmentalReverb if you wish,
        // like PRESET_STONEROOM, PRESET_AUDITORIUM, PRESET_CONCERTHALL, PRESET_CAVE, etc.
        // Just make sure the order here matches the order of the constants you use in `setReverb`.
    )
}