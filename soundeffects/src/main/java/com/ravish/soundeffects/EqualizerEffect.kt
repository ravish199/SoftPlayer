package com.ravish.soundeffects

import android.media.audiofx.EnvironmentalReverb
import android.media.audiofx.Equalizer
import android.media.audiofx.PresetReverb
import android.util.Log
import com.ravish.soundeffects.data.EqualizerPreset
import com.ravish.soundeffects.data.PresetRepository
import com.ravish.soundeffects.data.ReverbRepository
import com.ravish.soundeffects.usecases.UpdateBandLevels
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EqualizerEffect: AudioEffectManager {

    private var equalizer: Equalizer? = null
    private var presetReverb: PresetReverb? = null
    private var audioSessionId: Int = 0
    private var bandLevelRange: Pair<Short, Short>? = null

    private var noOfBands: Short = 0

    /**
     * Initializes the Equalizer for a given audio session.
     * Must be called before any other methods.
     *
     * @param sessionId The audio session ID from the media player (e.g., ExoPlayer.audioSessionId).
     * @return True if initialization was successful, false otherwise.
     */
    private fun init(sessionId: Int): Boolean {
        // Do not re-initialize if the session is the same and equalizer is already active
        if (sessionId == audioSessionId && equalizer != null) {
            return true
        }

        // Release any previous instance
        release()

        // An audio session ID of 0 is invalid.
        if (sessionId == 0) {
            Log.w("EqualizerEffect", "Cannot initialize with audio session ID 0.")
            return false
        }
        audioSessionId = sessionId

        return try {
            equalizer = Equalizer(0, audioSessionId).also {
        /*        it.enabled = true // Enable the equalizer
                Log.d("EqualizerEffect", "setEnabled: equalizer")*/
            }
            Log.d("EqualizerEffect", "Equalizer initialized for session ID $audioSessionId")
            /* environmentalReverb = EnvironmentalReverb(0, audioSessionId).also {
                 it.enabled = true // Enable the reverb effect
             }*/
            presetReverb= PresetReverb(0, audioSessionId).also {
             /*   it.enabled = true // Enable the reverb effect
                Log.d("EqualizerEffect", "setEnabled: presetReverb")*/
            }
            true
        } catch (e: Exception) {
            Log.e("EqualizerEffect", "Failed to initialize effects for session ID $audioSessionId", e)
            // 1. FIX: Call release() to clean up any partially initialized effects
            release()
            false
        }
    }



    /**
     * Gets the number of frequency bands supported by the device's equalizer.
     *
     * @return The number of bands, or 0 if not initialized.
     */
    private fun getNumberOfBands(): Short {
        return equalizer?.numberOfBands ?: 0
    }

    /**
     * Gets the valid gain range for the equalizer bands.
     *
     * @return A Pair where the first value is the minimum gain in millibels (mB)
     *         and the second is the maximum gain. Returns null if not initialized.
     */
    private fun getBandLevelRange(): Pair<Short, Short>? {
        return equalizer?.bandLevelRange?.let { range ->
            Pair(range[0], range[1])
        }
    }

    /**
     * Gets the center frequency of a specific band.
     *
     * @param band The index of the band to query.
     * @return The center frequency in Hertz (Hz), or 0 if not initialized or band is invalid.
     */
    private fun getCenterFreq(band: Short): Int {
        return equalizer?.getCenterFreq(band)?.div(1000) ?: 0 // Convert mHz to Hz
    }

    override fun initEqualizer(sessionId: Int?) {
        Log.d("initEqualizer", "sessionId: $sessionId")
        try {
            sessionId?.let {
                init(sessionId)
                noOfBands = getNumberOfBands()
                Log.d("Equalizer", "noOfBands: $noOfBands")
                bandLevelRange = getBandLevelRange()
                Log.d("Equalizer", "bandLevelRange: $bandLevelRange")
            }
        } catch (e: Exception) {
            Log.d("Equalizer", "Exception: $e")
        }
    }

    override fun enableEqualizer(enabled: Boolean) {
        Log.d("EqualizerEffect", "enableEqBands: $enabled")
        equalizer?.enabled = enabled
    }

    /**
     * Sets the gain for a specific frequency band.
     *
     * @param band The index of the band to modify.
     * @param level The desired gain in millibels (mB). This value will be clamped to the supported range.
     */
    override fun setBandLevel(band: Short, bandLevel: Float) {

        val bandValue = convertPercentToRange(bandLevel)
        Log.d("EqualizerEffect", "setBandLevel : $band, $bandValue")
        equalizer?.let { eq ->
            getBandLevelRange()?.let { (min, max) ->
                // Clamp the level to ensure it's within the valid range
                val clampedLevel = bandValue.coerceIn(min, max)
                eq.setBandLevel(band, clampedLevel)
            }
        }
    }

    override fun getBandLevels(): Array<Float>? {
        val bandLevels = Array<Float>(5){0f}
        for(i in 0..4) {
            bandLevels[i]= convertToPercent(equalizer?.getBandLevel(i.toShort()) ?: 0)
        }

        return bandLevels
    }

    private fun convertToPercent(value: Short): Float {
        return bandLevelRange?.second?.let { ((value + it)/2)/it }?.toFloat() ?: 0f
    }

    private fun convertPercentToRange(value: Float): Short {
        return bandLevelRange?.second?.let { (value * it * 2)-it}?.let { it.toInt().toShort() } ?: 0
    }

    override fun updateBandLevels(levels: Array<Float>) {
        if (levels.size.toShort() == noOfBands) {
            for (i in 0..noOfBands - 1) {
                equalizer?.setBandLevel(i.toShort(), levels[i].toInt().toShort())
            }
        }
    }

    override fun getPresetData(): List<EqualizerPreset> {
        return PresetRepository.getPresets()
    }

    override fun enableReverb(enable: Boolean) {
        presetReverb?.enabled = enable
        Log.d("EqualizerEffect", "enableReverb: $enable $presetReverb")
    }

    override fun getReverbData(): List<String> {
        return ReverbRepository.getReverbPresetNames
    }

    /*    override fun setReverb(presetIndex: Int) {
                audioEffectManager?.setEnvironmentalReverb(presetIndex.toShort())
      *//*          equalizerSettingsManager?.saveEnvironmentalReverb(presetIndex)
            equalizerUIState.environmentalReverb.value = presetIndex*//*
    }*/

    override fun setReverb(presetIndex: Int) {
        Log.d("EqualizerEffect", "Setting reverb preset to index: $presetIndex")
        try {
            presetReverb?.preset = presetIndex.toShort()

        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Gets the current gain level of a specific band.
     *
     * @param band The index of the band to query.
     * @return The current gain in millibels (mB), or 0 if not initialized or band is invalid.
     */
    private fun getBandLevel(band: Short): Short {
        return equalizer?.getBandLevel(band) ?: 0
    }

    /**
     * Releases the Equalizer instance to free up system resources.
     * It's crucial to call this when the equalizer is no longer needed (e.g., in ViewModel's onCleared() or Activity/Fragment's onDestroy()).
     */
    private fun release() {
        equalizer?.release()
        equalizer = null
        presetReverb?.release() // 5. Release the reverb object as well
        presetReverb = null
        audioSessionId = 0
        Log.d("EqualizerEffect", "Audio effects released.")
    }
}
