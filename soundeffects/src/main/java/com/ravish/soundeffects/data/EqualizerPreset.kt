package com.ravish.soundeffects.data

// Represents a single equalizer preset configuration
data class EqualizerPreset(
    val name: String,
    // Band levels are stored as Floats between 0.0f and 1.0f,
    // corresponding to the slider's value range.
    val bandLevels: List<Float>
)