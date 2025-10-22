package com.ravish.player

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession // For system integration
import com.ravish.player.data.model.SongItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicPlayer(private val context: Context) : PlaybackActionDataSource, MediaItemDataSource {


    var selectedIndex = MutableStateFlow(0)
    var songTitleState = MutableStateFlow("")
    var songArtistState = MutableStateFlow("")
    var categoryNameState = MutableStateFlow("All Songs")

    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    var songUpdater = MutableStateFlow<MediaItem?>(null)
    var isPlayingUpdater = MutableStateFlow(false)
    var currentPositionUpdater = MutableStateFlow(0L)
    var totalDurationUpdater = MutableStateFlow(0L)
    private var isPlaying = false


    private var currentPositionUpdateJob: Job? = null
    private val playerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

     var onPlayEndedUpdater = MutableStateFlow(false)


    private var _songSeekValueState = MutableStateFlow(0f)
    var songSeekValueState = _songSeekValueState.asStateFlow()
    val mediaCount = MutableStateFlow(0)

    var mediaItemsState = MutableStateFlow(emptyList<SongItem>())


    @OptIn(UnstableApi::class)
    fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {

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

                            Player.STATE_READY -> {
                                this@MusicPlayer.onPlayEndedUpdater.value = false
                                android.util.Log.d(
                                    "MyMusicPlayer",
                                    "State: Ready"
                                )
                            }

                            Player.STATE_ENDED -> {
                                this@MusicPlayer.onPlayEndedUpdater.value = true
                                android.util.Log.d(
                                    "MyMusicPlayer",
                                    "State: Ended"
                                )
                            }
                        }
                    }

                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        android.util.Log.e("MyMusicPlayer", "Player Error: ${error.message}", error)
                        // Handle errors, maybe show a toast or log
                    }

                    override fun onTracksChanged(tracks: Tracks) {
                        Log.d("MyMusicPlayer", "Tracks changed: $tracks")
                        getPlayer()?.duration?.let {
                            if (it > 0) {
                                totalDurationUpdater.value = it
                            }
                        }

                        selectedIndex.value = getPlayer()?.currentMediaItemIndex ?: 0
                        if (mediaItemsState.value.isNotEmpty()) {
                            songTitleState.value = mediaItemsState.value[selectedIndex.value].title
                            songArtistState.value =
                                mediaItemsState.value[selectedIndex.value].artist ?: ""
                        }

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
                        /*    CoroutineScope(Dispatchers.Main).launch {
                                val window = Timeline.Window()
                                try {
                                    val timeLine = timeline.getWindow(0, window)
                                    if (timeLine.durationMs > 0) {
                                        Log.d("MyMusicPlayer", "Duration: ${timeLine.durationMs}")
                                        _totalDurationUpdater.value = timeLine.durationMs
                                    }
                                } catch (e: ArrayIndexOutOfBoundsException) {
                                    e.printStackTrace()
                                }
                            }*/
                    }

                })
            }

            // Create a MediaSession for system integration (controls, notifications)
            try {
                mediaSession = MediaSession.Builder(context, exoPlayer!!).build()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }

            android.util.Log.d("MyMusicPlayer", "Player initialized.")
        } else {
            android.util.Log.d("MyMusicPlayer", "Player already initialized.")
        }
    }

    // Function to play a single song from a URI
    private fun playSingleSong(index: Int) {
        if (exoPlayer == null) {
            initializePlayer() // Ensure player is initialized
        }
        exoPlayer?.let { player ->
            try {
                player.seekTo(index, 0)
                player.prepare()
                player.playWhenReady = true // Start playback when ready
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
    }

    // Function to play a list of songs from URIs
    private fun addSongList(songs: List<SongItem>) {
        mediaItemsState.value = songs
        mediaCount.value = songs.size
        if (songs.isEmpty()) {
            android.util.Log.w("MyMusicPlayer", "Song list is empty.")
            return
        }
        if (exoPlayer == null) {
            initializePlayer()
        }

        exoPlayer?.let { player ->
            val mediaItems = songs.map { it.contentUri }.map { MediaItem.fromUri(it) }
            player.setMediaItems(mediaItems) // Set the playlist
            player.prepare()
            android.util.Log.d("MyMusicPlayer", "Playing song list. Count: ${songs.size}")
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

    /*
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
                if(isPlayingUpdater.value) {
                    exoPlayer?.stop()
                    exoPlayer?.clearMediaItems()
                    Log.d("MyMusicPlayer", "Stopped playback")
                }
            }
        }

        suspend fun next() {
            exoPlayer?.seekToNextMediaItem()
                songUpdater.emit(exoPlayer?.currentMediaItem)
        }

        fun previous() {
            playerScope.launch {
                exoPlayer?.seekToPreviousMediaItem()
            }
        }*/

    /*  fun seekTo(positionMs: Long) {
          playerScope.launch {
              exoPlayer?.seekTo(positionMs)
          }

      }*/

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
        isPlayingUpdater.value = true
        currentPositionUpdateJob = playerScope.launch {
            while (true) {
                exoPlayer?.currentPosition?.let { currentPos ->
                    Log.d("MyMusicPlayer", "current position: ${currentPos / 1000}")
                    currentPositionUpdater.value = currentPos
                }
                delay(1000)
            }
        }
        Log.d("MyMusicPlayer", "Started position updates job.")
    }

    private fun stopPositionUpdates() {
        isPlayingUpdater.value = false
        currentPositionUpdateJob?.cancel()
        currentPositionUpdateJob = null
        Log.d("MyMusicPlayer", "Stopped position updates job.")
    }


    override fun play() {
        playerScope.launch {
                exoPlayer?.playWhenReady = true
                if (songSeekValueState.value > 0) {
                    Log.d("PlayerControlUI:", "seek to:${songSeekValueState.value}")
                    seekTo((songSeekValueState.value * 1000L).toLong())
                }
        }
    }

    override fun play(index: Int) {
        playerScope.launch {
            //  selectedIndex.value = index
            playSingleSong(index)
            Log.d("MyMusicPlayer", "play playback")
        }
    }

    override fun pause() {
        playerScope.launch {
            exoPlayer?.playWhenReady = false
            Log.d("MyMusicPlayer", "Paused playback")
        }
    }

    override fun stop() {
        playerScope.launch {
            if (isPlayingUpdater.value) {
                exoPlayer?.stop()
                exoPlayer?.clearMediaItems()
                Log.d("MyMusicPlayer", "Stopped playback")
                _songSeekValueState.value = 0f
            }
        }
    }

    override fun next() {
        if (selectedIndex.value == mediaCount.value - 1) {
            exoPlayer?.seekTo(0, 0)
        } else {
            exoPlayer?.seekToNextMediaItem()
        }
    }

    override fun previous() {
        playerScope.launch {
            if (selectedIndex.value == 0) {
                exoPlayer?.seekTo(mediaCount.value - 1, 0)
            } else {
                exoPlayer?.seekToPreviousMediaItem()
            }
        }
    }

    override fun seekTo(positionMs: Long) {
        playerScope.launch {
            exoPlayer?.seekTo(positionMs)
        }
    }

    override fun enableShuffle(enable: Boolean) {
        playerScope.launch {
            exoPlayer?.shuffleModeEnabled = enable
        }
    }

    override fun setRepeatMode(repeatMode: Int) {
        playerScope.launch {
            exoPlayer?.repeatMode = repeatMode
        }
    }


    override fun addMediaItemList(audioList: List<SongItem>) {
        addSongList(audioList)
    }

    override fun updateMediaItemIndex(index: Int) {
        //selectedIndex.value = index
    }

    @OptIn(UnstableApi::class)
    fun getAudioSessionId(): Int? = exoPlayer?.audioSessionId

}

