package com.ravish.softplayer.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.ravish.player.MediaFileManager
import com.ravish.player.MusicPlayer
import com.ravish.player.usecases.Pause
import com.ravish.player.usecases.Play
import com.ravish.player.usecases.PlayNext
import com.ravish.player.usecases.PlayPrevious
import com.ravish.player.usecases.PlaySelectedSong
import com.ravish.player.usecases.SeekTo
import com.ravish.player.usecases.SetRepeatMode
import com.ravish.player.usecases.Stop
import com.ravish.player.usecases.UpdateShuffleState
import com.ravish.softplayer.data.EqualizerSettingsManager
import com.ravish.softplayer.data.model.EqualizerUIState
import com.ravish.softplayer.data.model.MediaUpdateUIState
import com.ravish.softplayer.data.model.PlayBackUIState
import com.ravish.softplayer.data.model.RepeatMode
import com.ravish.softplayer.data.model.SliderUIState
import com.ravish.softplayer.data.model.SongCategoryUIState
import com.ravish.softplayer.data.model.SongInfoUIState
import com.ravish.softplayer.data.model.SoundEffectUseCases
import com.ravish.softplayer.data.model.UseCases
import com.ravish.soundeffects.AudioEffectManager
import com.ravish.soundeffects.data.EqualizerPreset
import com.ravish.soundeffects.usecases.EnableEqualizer
import com.ravish.soundeffects.usecases.InitializeEqualizer
import com.ravish.soundeffects.usecases.SetBandLevel
import com.ravish.soundeffects.usecases.UpdateBandLevels
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PlayerViewModel @Inject constructor() : ViewModel() {

    private var useCases: UseCases? = null
    private var equalizerSettingsManager: EqualizerSettingsManager? = null


    private var soundEffectUseCases: SoundEffectUseCases? = null
    var songCategoryUIState: SongCategoryUIState? = null
    var songInfoUIState: SongInfoUIState? = null
    var sliderUIState: SliderUIState? = null
    var mediaUpdateUIState: MediaUpdateUIState? = null
    var playBackUIState: PlayBackUIState? = null
    private var playerBackgroundState = MutableStateFlow<Bitmap?>(null)
    private var _enableListMode = MutableStateFlow(false)
    var enableListMode = _enableListMode.asStateFlow()

    private var _openEqualizerState = MutableStateFlow(false)
    var openEqualizerState = _openEqualizerState.asStateFlow()


   private var _savedEqualizerBandlevels= MutableStateFlow<List<Float>?>(emptyList())
    var savedEqualizerBandlevels = _savedEqualizerBandlevels.asStateFlow()
   private var _enableEqualizerState = MutableStateFlow(false)
    var enableEqualizerState = _enableEqualizerState.asStateFlow()


    var audioEffectManager: AudioEffectManager? = null

    var musicPlayer: MusicPlayer? = null
    var equalizerUIState: EqualizerUIState? = null

    private var _updatePresetBand = MutableStateFlow<List<Float>>(emptyList())
    var updatePresetBand = _updatePresetBand.asStateFlow()

    private var _presetName = MutableStateFlow<String>("")
    var presetName = _presetName.asStateFlow()

    fun updateEqualizeView() {
        _openEqualizerState.value = !openEqualizerState.value
    }

    fun closeEqualizer() {
        _openEqualizerState.value = false

    }

    fun setPlayerBackground(bitmap: Bitmap?) {
        playerBackgroundState.value = bitmap
    }


    fun playSelected(index: Int) {
        useCases?.playSelected?.let { it(index) }
    }

    fun play() {
        useCases?.play?.let { it() }
    }

    fun previous() {
        useCases?.playPrevious?.let { it() }
    }

    fun next() {
        useCases?.playNext?.let { it() }
    }

    fun pause() {
        useCases?.pause?.let { it() }
    }

    fun seekTo(positionMs: Long) {
        useCases?.seekTo?.let { it(positionMs) }
    }

    fun shuffle(enable: Boolean) {
        useCases?.shuffle?.let { it(enable) }
    }

    fun stop() {
        useCases?.stop?.let { it() }
    }

    fun enableListMode(enable: Boolean) {
        _enableListMode.value = enable
    }

    fun initializeEqualizer() {
        viewModelScope.launch {
            soundEffectUseCases?.initializeEqualizer?.invoke(sessionId = musicPlayer?.getAudioSessionId())
            _savedEqualizerBandlevels.value = equalizerSettingsManager?.eqBandLevelsFlow?.first()
            _enableEqualizerState.value = equalizerSettingsManager?.eqEnabledFlow?.first() ?: false
          //  _presetName.value = equalizerSettingsManager?.presetNameFlow?.first().toString()
            Log.d("PlayerViewModel", "initializeEqualizer: ${_presetName.value}")
            Log.d("PlayerViewModel", "initializeEqualizer: ${equalizerSettingsManager?.presetNameFlow.hashCode()}")
   /*         soundEffectUseCases?.enableEqualizer?.invoke(savedEnabledState ?: false)
            soundEffectUseCases?.updateBandLevel?.invoke(savedLevels?.toTypedArray() ?: emptyArray())*/
        }

    }




    /**
     * Call this from your UI when a slider value changes and settles.
     */
    fun updateAndSaveBandLevel(bandIndex: Int, level: Float) {
        viewModelScope.launch {
            // Set the level in the audio effect
            soundEffectUseCases?.setBandLevel?.invoke(bandIndex.toShort(), level)
           // equalizerSettingsManager?.saveBandLevels(audioEffectManager?.getBandLevels()?.toList())
        }
    }


     fun updatePresetBands(presetName: String, levels: List<Float>) {
         Log.d("PlayerViewModel", "presetName: $presetName")
        Log.d("PlayerViewModel", "updatePresetBands: $levels")
         _presetName.value = presetName
        _updatePresetBand.value = levels
        levels.forEachIndexed { index, preset ->
            setBandLevel(index, preset)
        }

    }

    fun savePreset(presetName: String) {
        viewModelScope.launch {
            // equalizerSettingsManager?.saveBandLevels(levels)
            equalizerSettingsManager?.savePreset(presetName)
        }
    }

    /**
     * Call this from your UI when the main equalizer switch is toggled.
     */
    fun updateAndSaveEqEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            _enableEqualizerState.value = isEnabled
            soundEffectUseCases?.enableEqualizer?.invoke(isEnabled)
            equalizerSettingsManager?.saveEqEnabled(isEnabled)
        }
    }

    fun setBandLevel(bandIndex: Int, level: Float) {
        viewModelScope.launch {
            soundEffectUseCases?.setBandLevel?.invoke(bandIndex.toShort(), level)
        }
    }

    fun getPresetData():List<EqualizerPreset> {
        return audioEffectManager?.getPresetData() ?: emptyList()
    }


    fun setRepeatMode(repeatMode: RepeatMode) {
        val rMode = when (repeatMode) {
            RepeatMode.REPEAT_NONE -> Player.REPEAT_MODE_OFF
            RepeatMode.REPEAT_ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.REPEAT_ALL -> Player.REPEAT_MODE_ALL
        }
        useCases?.setRepeatMode?.invoke(rMode)
    }


    fun initMusicPlayer(musicPlayer: MusicPlayer?,
                        audioEffectManager: AudioEffectManager?,
                        equalizerSettingsManager: EqualizerSettingsManager) {
        this.equalizerSettingsManager = equalizerSettingsManager
        musicPlayer?.let {
            initUseCases(it)
            initSongCategoryUIState(it)
            initSongInfoUIState(it)
            initSliderUiState(it)
            initMediaUpdateUIState(it)
            initPlayBackUIState(it)
            this.musicPlayer = it
        }
        audioEffectManager?.let {
            initAudioEffects(audioEffectManager = it)
        }
    }

    private fun initAudioEffects(audioEffectManager: AudioEffectManager) {
        soundEffectUseCases = SoundEffectUseCases(
            initializeEqualizer = InitializeEqualizer(audioEffectManager = audioEffectManager),
            enableEqualizer = EnableEqualizer(audioEffectManager = audioEffectManager),
            updateBandLevel = UpdateBandLevels(audioEffectManager = audioEffectManager),
            setBandLevel = SetBandLevel(audioEffectManager = audioEffectManager)
        )
        this.audioEffectManager = audioEffectManager
    }

    private fun initPlayBackUIState(musicPlayer: MusicPlayer) {
        playBackUIState = PlayBackUIState(
            musicPlayer.isPlayingUpdater.asStateFlow(),
            musicPlayer.onPlayEndedUpdater.asStateFlow()
        )
    }

    private fun initMediaUpdateUIState(musicPlayer: MusicPlayer) {
        mediaUpdateUIState = MediaUpdateUIState(
            musicPlayer.mediaItemsState.asStateFlow()
        )
    }

    private fun initSliderUiState(musicPlayer: MusicPlayer) {
        sliderUIState = SliderUIState(
            musicPlayer.totalDurationUpdater.asStateFlow(),
            musicPlayer.currentPositionUpdater.asStateFlow()
        )
    }

    private fun initSongCategoryUIState(musicPlayer: MusicPlayer) {
        songCategoryUIState = SongCategoryUIState(
            musicPlayer.categoryNameState.asStateFlow(),
            musicPlayer.mediaCount.asStateFlow(),
            musicPlayer.selectedIndex.asStateFlow()
        )
    }

    private fun initSongInfoUIState(musicPlayer: MusicPlayer) {
        songInfoUIState = SongInfoUIState(
            musicPlayer.songTitleState.asStateFlow(),
            musicPlayer.songArtistState.asStateFlow(),
            playerBackgroundState.asStateFlow()
        )
    }

    private fun initUseCases(musicPlayer: MusicPlayer?) {
        musicPlayer?.let {
            useCases = UseCases(
                playSelected = PlaySelectedSong(it),
                play = Play(it),
                playNext = PlayNext(it),
                playPrevious = PlayPrevious(it),
                pause = Pause(it),
                seekTo = SeekTo(it),
                stop = Stop(it),
                shuffle = UpdateShuffleState(it),
                setRepeatMode = SetRepeatMode(it)
            )
        }
    }

    fun loadAudioFiles(musicPlayer: MusicPlayer?, fileManager: MediaFileManager?) {
        viewModelScope.launch {
            fileManager?.loadAudioFiles {
                musicPlayer?.addMediaItemList(it)
            }
        }
    }
}