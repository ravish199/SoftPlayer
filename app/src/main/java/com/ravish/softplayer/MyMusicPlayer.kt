/*
package com.ravish.softplayer

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession // For system integration
import com.ravish.softplayer.Utils.Companion.ioScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyMusicPlayer(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    var songUpdater = MutableStateFlow<MediaItem?>(null)
    var isPlayingUpdater= MutableStateFlow<Boolean>(false)
    var currentPositionUpdater = MutableStateFlow(0L)
    var totalDurationUpdater = MutableStateFlow(0L)


    // Call this to initialize the player
    fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                // Optional: Add a listener for player events
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        // Handle play/pause state changes
                        // e.g., update UI
                        ioScope.launch {
                            this@MyMusicPlayer.isPlayingUpdater.emit(isPlaying)
                        }
                        if (isPlaying) {
                            android.util.Log.d("MyMusicPlayer", "Playback started.")
                        } else {
                            android.util.Log.d("MyMusicPlayer", "Playback paused or stopped.")
                        }
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_IDLE -> android.util.Log.d("MyMusicPlayer", "State: Idle")
                            Player.STATE_BUFFERING -> android.util.Log.d("MyMusicPlayer", "State: Buffering")
                            Player.STATE_READY -> android.util.Log.d("MyMusicPlayer", "State: Ready")
                            Player.STATE_ENDED -> android.util.Log.d("MyMusicPlayer", "State: Ended")
                        }
                    }

                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        android.util.Log.e("MyMusicPlayer", "Player Error: ${error.message}", error)
                        // Handle errors, maybe show a toast or log
                    }

                    override fun onTracksChanged(tracks: Tracks) {
                        super.onTracksChanged(tracks)
                    }

                    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                        super.onShuffleModeEnabledChanged(shuffleModeEnabled)
                    }

                    override fun onEvents(player: Player, events: Player.Events) {
                        super.onEvents(player, events)
                        ioScope.launch {
                            currentPositionUpdater.emit(player.currentPosition)
                        }
                    }

                    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                        super.onTimelineChanged(timeline, reason)
                        ioScope.launch {
                            val window = Timeline.Window()
                            val timeLine = timeline.getWindow(0, window)
                            totalDurationUpdater.emit(timeLine.durationMs)
                        }
                    }

                })
            }

            // Create a MediaSession for system integration (controls, notifications)
            mediaSession = MediaSession.Builder(context, exoPlayer!!).build()
            android.util.Log.d("MyMusicPlayer", "Player initialized.")
        } else {
            android.util.Log.d("MyMusicPlayer", "Player already initialized.")
        }
    }

    // Function to play a single song from a URI
    fun playSingleSong(songUri: Uri) {
        if (exoPlayer == null) {
            initializePlayer() // Ensure player is initialized
        }
        exoPlayer?.let { player ->
            val mediaItem = MediaItem.fromUri(songUri)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true // Start playback when ready
            android.util.Log.d("MyMusicPlayer", "Playing single song: $songUri")
        }
    }

    // Function to play a list of songs from URIs
    fun playSongList(songUris: List<Uri>) {
        if (songUris.isEmpty()) {
            android.util.Log.w("MyMusicPlayer", "Song list is empty.")
            return
        }
        if (exoPlayer == null) {
            initializePlayer()
        }
        exoPlayer?.let { player ->
            val mediaItems = songUris.map { MediaItem.fromUri(it) }
            player.setMediaItems(mediaItems) // Set the playlist
            player.prepare()
            player.playWhenReady = true
            android.util.Log.d("MyMusicPlayer", "Playing song list. Count: ${songUris.size}")
        }
    }

    // Function to add a song to the current playlist
    fun addSongToQueue(songUri: Uri) {
        exoPlayer?.let { player ->
            val mediaItem = MediaItem.fromUri(songUri)
            player.addMediaItem(mediaItem)
            android.util.Log.d("MyMusicPlayer", "Added to queue: $songUri")
        }
    }


    // Playback control functions
    fun play() {
        exoPlayer?.playWhenReady = true

    }

    fun pause() {
        exoPlayer?.playWhenReady = false
    }

    fun stop() {
        exoPlayer?.stop()
        exoPlayer?.clearMediaItems() // Optionally clear the playlist
    }

    fun next() {
        exoPlayer?.seekToNextMediaItem()
        ioScope.launch {
            songUpdater.emit(exoPlayer?.currentMediaItem)
        }
    }

    fun previous() {
        exoPlayer?.seekToPreviousMediaItem()
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
    }

    // Call this when the player is no longer needed (e.g., in onStop or onDestroy)
    fun releasePlayer() {
        mediaSession?.release() // Release the session first
        mediaSession = null
        exoPlayer?.release()    // Then release the player
        exoPlayer = null
        android.util.Log.d("MyMusicPlayer", "Player released.")
    }

    // Optional: Expose player for UI updates or advanced control
    fun getPlayer(): Player? = exoPlayer

    fun getMediaSession(): MediaSession? = mediaSession
}
*/
