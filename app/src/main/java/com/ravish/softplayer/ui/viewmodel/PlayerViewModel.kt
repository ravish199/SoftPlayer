package com.ravish.softplayer.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.ravish.player.MediaFileManager
import com.ravish.player.MusicPlayer
import com.ravish.player.data.model.SongItem
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
import com.ravish.softplayer.data.model.RepeatMode
import com.ravish.softplayer.data.model.SliderUIState
import com.ravish.softplayer.data.model.SoundEffectUseCases
import com.ravish.softplayer.data.model.UseCases
import com.ravish.softplayer.data.model.uistate.EqualizerUIState
import com.ravish.softplayer.data.model.uistate.MediaUpdateUIState
import com.ravish.softplayer.data.model.uistate.PlayBackUIState
import com.ravish.softplayer.data.model.uistate.SongCategoryUIState
import com.ravish.softplayer.data.model.uistate.SongInfoUIState
import com.ravish.softplayer.data.model.uistate.TrackListUIState
import com.ravish.soundeffects.AudioEffectManager
import com.ravish.soundeffects.data.EqualizerPreset
import com.ravish.soundeffects.usecases.EnableEqualizer
import com.ravish.soundeffects.usecases.EnableReverb
import com.ravish.soundeffects.usecases.InitializeEqualizer
import com.ravish.soundeffects.usecases.SetBandLevel
import com.ravish.soundeffects.usecases.UpdateBandLevels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PlayerViewModel @Inject constructor() : ViewModel() {

    private var useCases: UseCases? = null
    private var equalizerSettingsManager: EqualizerSettingsManager? = null


    private var soundEffectUseCases: SoundEffectUseCases? = null
    lateinit var songCategoryUIState: SongCategoryUIState
    lateinit var songInfoUIState: SongInfoUIState
    var sliderUIState: SliderUIState? = null
    lateinit var mediaUpdateUIState: MediaUpdateUIState
    lateinit var trackListUIState: TrackListUIState
    lateinit var playBackUIState: PlayBackUIState
    private var _playerBackgroundState = MutableStateFlow<Bitmap?>(null)
    private var _enableListMode = MutableStateFlow(false)
    var enableListMode = _enableListMode.asStateFlow()

    private var _openEqualizerState = MutableStateFlow(false)
    private var _filterTrack = MutableStateFlow("")

    private var _currentReverbState = MutableStateFlow(0)
    private var _updateReverbState = MutableStateFlow(0)

    private var _openTrackList = MutableStateFlow(false)
    private var _playingTrack = MutableStateFlow<SongItem?>(null)


    private var _savedEqualizerBandlevels = MutableStateFlow<List<Float>?>(emptyList())
    var savedEqualizerBandlevels = _savedEqualizerBandlevels.asStateFlow()
    private var _enableEqualizerState = MutableStateFlow(false)

    private var _enableReverbState = MutableStateFlow(false)


    var audioEffectManager: AudioEffectManager? = null

    var musicPlayer: MusicPlayer? = null
    lateinit var equalizerUIState: EqualizerUIState

    private var _updatePresetBand = MutableStateFlow<List<Float>>(emptyList())
    private var _presetName = MutableStateFlow<String>("")

    fun updateEqualizeView() {
        _openEqualizerState.value = !(equalizerUIState?.openEqualizerState?.value ?: false)
    }

    fun openTrackList() {
        Log.d("PlayerViewModel", "openTrackList:${_openTrackList.value}")
        _openTrackList.value = true
    }

    fun closeTrackList() {
        Log.d("PlayerViewModel", "closeTrackList:${_openTrackList.value}")
        _openTrackList.value = false
    }

    fun closeEqualizer() {
        _openEqualizerState.value = false

    }

    fun filterTrackList(query: String) {
        _filterTrack.value = query
    }


    fun setPlayerBackground(bitmap: Bitmap) {
        _playerBackgroundState.value = bitmap
    }


    fun playSelected(index: Int, song: SongItem? = null) {
        useCases?.playSelected?.let { it(index) }
        _playingTrack.value = song
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
            if (equalizerSettingsManager?.eqEnabledFlow?.first() == false) {
                soundEffectUseCases?.initializeEqualizer?.invoke(sessionId = musicPlayer?.getAudioSessionId())
            }
            _savedEqualizerBandlevels.value = equalizerSettingsManager?.eqBandLevelsFlow?.first()
            _enableEqualizerState.value = equalizerSettingsManager?.eqEnabledFlow?.first() ?: false
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


    fun updatePresetBands(levels: List<Float>) {
        Log.d("PlayerViewModel", "updatePresetBands: $levels")
        _updatePresetBand.value = levels
        levels.forEachIndexed { index, preset ->
            setBandLevel(index, preset)
        }

    }


    fun savePreset(presetName: String) {
        viewModelScope.launch {
            equalizerSettingsManager?.savePreset(presetName)
        }
    }

    fun getPreset() {
        viewModelScope.launch {
            equalizerSettingsManager?.presetNameFlow?.collect {
                _presetName.value = it
            }

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

    fun enableReverb(enable: Boolean) {
        viewModelScope.launch {
            _enableReverbState.value = enable
            soundEffectUseCases?.enableReverb?.invoke(enable)
        }
    }

    fun setBandLevel(bandIndex: Int, level: Float) {
        viewModelScope.launch {
            soundEffectUseCases?.setBandLevel?.invoke(bandIndex.toShort(), level)
        }
    }

    fun getPresetData(): List<EqualizerPreset> {
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


    fun initMusicPlayer(
        musicPlayer: MusicPlayer?,
        audioEffectManager: AudioEffectManager?,
        equalizerSettingsManager: EqualizerSettingsManager
    ) {
        this.equalizerSettingsManager = equalizerSettingsManager
        musicPlayer?.let {
            initUseCases(it)
            initSongCategoryUIState(it)
            initSongInfoUIState(it)
            initSliderUiState(it)
            initMediaUpdateUIState(it)
            initPlayBackUIState(it)
            initEqualizerUiState()
            initTrackListUIState()
            this.musicPlayer = it
        }
        audioEffectManager?.let {
            initAudioEffects(audioEffectManager = it)
        }

        initEqualizer()

    }

    private fun initEqualizer() {
        viewModelScope.launch {
            if (equalizerSettingsManager?.eqEnabledFlow?.first() == true) {
                soundEffectUseCases?.initializeEqualizer?.invoke(sessionId = musicPlayer?.getAudioSessionId())
                val presetName = equalizerSettingsManager?.presetNameFlow?.first()
                with(getPresetData()) {
                    val index = this.map { it.name }.indexOf(presetName)
                    if (index != -1) {
                        _updatePresetBand.value = this[index].bandLevels
                    }
                    updatePresetBands(
                        levels = _updatePresetBand.value
                    )
                }
            }
        }

    }

    private fun initEqualizerUiState() {
        equalizerUIState = EqualizerUIState(
            openEqualizerState = _openEqualizerState.asStateFlow(),
            enableEqualizerState = _enableEqualizerState.asStateFlow(),
            updatePresetBand = _updatePresetBand.asStateFlow(),
            presetName = _presetName.asStateFlow(),
            enableReverbState = _enableReverbState.asStateFlow(),
            currentReverb = _currentReverbState.asStateFlow(),
            updateReverb = _updateReverbState.asStateFlow()
        )
    }

    private fun initAudioEffects(audioEffectManager: AudioEffectManager) {
        soundEffectUseCases = SoundEffectUseCases(
            initializeEqualizer = InitializeEqualizer(audioEffectManager = audioEffectManager),
            enableEqualizer = EnableEqualizer(audioEffectManager = audioEffectManager),
            enableReverb = EnableReverb(audioEffectManager = audioEffectManager),
            updateBandLevel = UpdateBandLevels(audioEffectManager = audioEffectManager),
            setBandLevel = SetBandLevel(audioEffectManager = audioEffectManager)
        )
        this.audioEffectManager = audioEffectManager
    }

    private fun initPlayBackUIState(musicPlayer: MusicPlayer) {
        playBackUIState = PlayBackUIState(
            musicPlayer.isPlayingUpdater.asStateFlow(),
            musicPlayer.onPlayEndedUpdater.asStateFlow(),
            playingTrack = _playingTrack.asStateFlow()
        )
    }

    private fun initMediaUpdateUIState(musicPlayer: MusicPlayer) {
        mediaUpdateUIState = MediaUpdateUIState(
            musicPlayer.mediaItemsState.asStateFlow()
        )
    }

    private fun initTrackListUIState() {
        trackListUIState = TrackListUIState(
            _openTrackList.asStateFlow(),
            _filterTrack.asStateFlow()
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
            musicPlayer.songAlbumState.asStateFlow(),
            musicPlayer.songArtistState.asStateFlow(),
            musicPlayer.songUriState.asStateFlow(),
            _playerBackgroundState.asStateFlow()
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

    fun getReverbPresetNames() = audioEffectManager?.getReverbData()

    fun setCurrentReverb(reverbIndex: Int) {
        _currentReverbState.value = reverbIndex
    }

    fun updateReverb(reverbIndex: Int) {
        _updateReverbState.value = reverbIndex
        audioEffectManager?.setReverb(reverbIndex)
    }

}