package com.ravish.softplayer

import android.net.Uri

data class SongItem(
    val id: Long,
    val title: String,
    val artist: String?,
    val album: String?,
    val duration: Long, // in milliseconds
    val data: String, // File path
    val contentUri: Uri
) {

}


// Helper function to format duration (Long milliseconds to MM:SS String)
fun formatDuration(millis: Long): String {
    val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(millis) -
            java.util.concurrent.TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%02d:%02d", minutes, seconds)
}