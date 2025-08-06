package com.ravish.player

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.ravish.player.data.model.SongItem
import kotlinx.coroutines.delay

class MediaFileManager(private val context: Context) {

    private var songList: List<SongItem>? = null
    private var currentSong: SongItem? = null
    var audioList:List<SongItem>? = null

     suspend fun loadAudioFiles(onLoaded: suspend (List<SongItem>) -> Unit, loadProgress: suspend (Int, Int) -> Unit) {

         // Implementation in the next step
        // For now, let's just log or show a toast
       queryAudioFiles(onLoaded, loadProgress)/*
        // Do something with audioList, e.g., display in a RecyclerView
        audioList.forEach { audioFile ->
            android.util.Log.d("AudioFiles", "Title: ${audioFile.title}, Path: ${audioFile.data}")
        }*/
    }


    // Add this function to your Activity or a Repository class
   private suspend fun queryAudioFiles(onLoaded: suspend (List<SongItem>) -> Unit, loadProgress: suspend (Int, Int) -> Unit) {
        val audioList = mutableListOf<SongItem>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA // File path (deprecated for direct access in Android 10+, use URI)
        )

        // Filter to ensure we only get music files (optional, but good practice)
        // IS_MUSIC looks for typical audio files, adjust if you need other types like ringtones
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC" // Sort by title

        // For Android 10 (API 29) and above, it's recommended to use volume-specific URIs
        val queryUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        context.contentResolver.query( // Use applicationContext to avoid leaks if in a bg thread
            queryUri,
            projection,
            selection,
            null, // No selection arguments for this basic query
            sortOrder
        )?.use { cursor -> // 'use' ensures the cursor is closed automatically
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            var count = 0
            val total = cursor.count
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val duration = cursor.getLong(durationColumn)
                val data = cursor.getString(dataColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // Base URI for creating the content URI
                    id
                )

                audioList.add(SongItem(id, title, artist, album, duration, data, contentUri))
                Log.d("LoadSongs:", "Loading: count:${count}, total:${total}")
                delay(1L)
                loadProgress.invoke(++count, total)
            }
            onLoaded.invoke(audioList)
        }
    }


/*    fun loadAudioFiles(onLoaded: () -> Unit) {
            songList = queryAudioFiles()
            // Do something with audioList, e.g., display in a RecyclerView
            songList?.forEach { audioFile ->
                android.util.Log.d(
                    "AudioFiles",
                    "Title: ${audioFile.title}, Path: ${audioFile.data}"
                )
            }
            currentSong = songList?.get(0)
            withContext(Dispatchers.Main) {
                onLoaded.invoke()
            }
        }*/

}