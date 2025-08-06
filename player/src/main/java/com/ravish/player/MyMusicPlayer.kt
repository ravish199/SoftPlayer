package com.ravish.player

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession // For system integration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyMusicPlayer(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    var songUpdater = MutableStateFlow<MediaItem?>(null)
    var isPlayingUpdater = MutableStateFlow<Boolean>(false)
    private var _currentPositionUpdater = MutableStateFlow(0L)
   private var _totalDurationUpdater = MutableStateFlow(0L)

    var currentPositionUpdater =_currentPositionUpdater
    var totalDurationUpdater = _totalDurationUpdater
    var currentPositionUpdateJob: Job? = null
    private val playerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())


    // Call this to initialize the player
    fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                // Optional: Add a listener for player events
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        // Handle play/pause state changes
                        // e.g., update UI
                        CoroutineScope(Dispatchers.IO).launch {
                            this@MyMusicPlayer.isPlayingUpdater.emit(isPlaying)
                        }
                        if (isPlaying) {
                            startPositionUpdates()
                            android.util.Log.d("MyMusicPlayer", "Playback started.")
                        } else {
                            stopPositionUpdates()
                            android.util.Log.d("MyMusicPlayer", "Playback paused or stopped.")
                        }
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_IDLE -> android.util.Log.d("MyMusicPlayer", "State: Idle")
                            Player.STATE_BUFFERING -> android.util.Log.d(
                                "MyMusicPlayer",
                                "State: Buffering"
                            )

                            Player.STATE_READY -> android.util.Log.d(
                                "MyMusicPlayer",
                                "State: Ready"
                            )

                            Player.STATE_ENDED -> android.util.Log.d(
                                "MyMusicPlayer",
                                "State: Ended"
                            )
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
                    }

                    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                        super.onTimelineChanged(timeline, reason)
                        CoroutineScope(Dispatchers.Main).launch {
                            val window = Timeline.Window()
                            val timeLine = timeline.getWindow(0, window)
                            _totalDurationUpdater.value = timeLine.durationMs
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
        playerScope.launch {
            exoPlayer?.playWhenReady = true
        }

    }

    fun pause() {
        playerScope.launch {
            exoPlayer?.playWhenReady = false
        }
    }

    fun stop() {
        playerScope.launch {
            exoPlayer?.stop()
            exoPlayer?.clearMediaItems() // Optionally clear the playlist
        }
    }

    fun next() {
        exoPlayer?.seekToNextMediaItem()
        CoroutineScope(Dispatchers.IO).launch {
            songUpdater.emit(exoPlayer?.currentMediaItem)
        }
    }

    fun previous() {
        playerScope.launch {
            exoPlayer?.seekToPreviousMediaItem()
        }
    }

    fun seekTo(positionMs: Long) {
        playerScope.launch {
            exoPlayer?.seekTo(positionMs)
        }

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





    private fun startPositionUpdates() {
        stopPositionUpdates() // Ensure only one update job is running
        currentPositionUpdateJob = playerScope.launch {
            while (true) {
                exoPlayer?.currentPosition?.let { currentPos ->
                    _currentPositionUpdater.value = currentPos
                }
                delay(1000)
            }
        }
        Log.d("MyMusicPlayer", "Started position updates job.")
    }

    private fun stopPositionUpdates() {
        currentPositionUpdateJob?.cancel()
        currentPositionUpdateJob = null
        Log.d("MyMusicPlayer", "Stopped position updates job.")
    }
}
