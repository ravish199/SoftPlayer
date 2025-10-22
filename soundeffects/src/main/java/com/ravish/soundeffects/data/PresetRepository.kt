package com.ravish.soundeffects.data

// Represents a single equalizer preset configuration


/**
 * Provides a predefined list of 10 common equalizer presets for a 5-band setup.
 * The values are normalized between 0.0f (min gain) and 1.0f (max gain).
 * The center (0 dB) is 0.5f.
 *
 * Bands are typically ordered from lowest frequency to highest:
 * Band 0: ~60 Hz (Bass)
 * Band 1: ~230 Hz (Low-Mid)
 * Band 2: ~910 Hz (Mid)
 * Band 3: ~3.6 kHz (Upper-Mid)
 * Band 4: ~14 kHz (Treble)
 */
internal object PresetRepository {
    fun getPresets(): List<EqualizerPreset> {
        return listOf(
            EqualizerPreset("Normal", listOf(0.5f, 0.5f, 0.5f, 0.5f, 0.5f)),
            EqualizerPreset("Classical", listOf(0.5f, 0.5f, 0.5f, 0.7f, 0.8f)),
            EqualizerPreset("Dance", listOf(0.8f, 0.6f, 0.5f, 0.6f, 0.7f)),
            EqualizerPreset("Flat", listOf(0.5f, 0.5f, 0.5f, 0.5f, 0.5f)),
            EqualizerPreset("Folk", listOf(0.6f, 0.6f, 0.5f, 0.6f, 0.5f)),
            EqualizerPreset("Heavy Metal", listOf(0.7f, 0.5f, 0.8f, 0.7f, 0.6f)),
            EqualizerPreset("Hip Hop", listOf(0.8f, 0.7f, 0.5f, 0.6f, 0.7f)),
            EqualizerPreset("Jazz", listOf(0.7f, 0.6f, 0.5f, 0.6f, 0.7f)),
            EqualizerPreset("Pop", listOf(0.4f, 0.6f, 0.7f, 0.6f, 0.4f)),
            EqualizerPreset("Rock", listOf(0.8f, 0.6f, 0.4f, 0.6f, 0.8f))
        )
    }
}
