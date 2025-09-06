package com.ravish.softplayer.ui.viewmodel

import android.graphics.Bitmap
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
import com.ravish.softplayer.data.model.MediaUpdateUIState
import com.ravish.softplayer.data.model.PlayBackUIState
import com.ravish.softplayer.data.model.RepeatMode
import com.ravish.softplayer.data.model.SliderUIState
import com.ravish.softplayer.data.model.SongCategoryUIState
import com.ravish.softplayer.data.model.SongInfoUIState
import com.ravish.softplayer.data.model.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {

    private var useCases: UseCases? = null
    var songCategoryUIState: SongCategoryUIState? = null
    var songInfoUIState: SongInfoUIState? = null
    var sliderUIState: SliderUIState? = null
    var mediaUpdateUIState: MediaUpdateUIState? = null
    var playBackUIState: PlayBackUIState? = null
    private var playerBackgroundState = MutableStateFlow<Bitmap?>(null)
    private var _enableListMode = MutableStateFlow(false)
    var enableListMode = _enableListMode.asStateFlow()


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


    fun setRepeatMode(repeatMode: RepeatMode) {
        val rMode = when (repeatMode) {
            RepeatMode.REPEAT_NONE -> Player.REPEAT_MODE_OFF
            RepeatMode.REPEAT_ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.REPEAT_ALL -> Player.REPEAT_MODE_ALL
        }
        useCases?.setRepeatMode?.invoke(rMode)
    }


    fun initMusicPlayer(musicPlayer: MusicPlayer?) {
        musicPlayer?.let {
            initUseCases(it)
            initSongCategoryUIState(it)
            initSongInfoUIState(it)
            initSliderUiState(it)
            initMediaUpdateUIState(it)
            initPlayBackUIState(it)
        }
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