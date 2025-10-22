package com.ravish.player

import android.graphics.Bitmap
import com.ravish.player.data.model.SongItem
import kotlinx.coroutines.flow.StateFlow
import java.lang.Thread.State

interface PlayCallbackDataSource {
    fun isPlaying(musicPlayer: MusicPlayer): StateFlow<Boolean>
    fun onCurrentPositionUpdate(musicPlayer: MusicPlayer): StateFlow<Long>
    fun onTotalDurationUpdate(musicPlayer: MusicPlayer): StateFlow<Long>
    fun onPlayEnded(musicPlayer: MusicPlayer):StateFlow<Boolean>
    fun onSongIndexUpdate(musicPlayer: MusicPlayer): StateFlow<Int>
    fun onSongTitleUpdate(musicPlayer: MusicPlayer): StateFlow<String>
    fun onArtistsUpdate(musicPlayer: MusicPlayer): StateFlow<String>
    fun totalMediaCount(musicPlayer: MusicPlayer): StateFlow<Int>
    fun onMediaItemsUpdate(musicPlayer: MusicPlayer): StateFlow<List<SongItem>>
}