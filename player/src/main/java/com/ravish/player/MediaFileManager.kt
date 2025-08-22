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
    var audioList:List<SongItem>? = null
     suspend fun loadAudioFiles(onLoaded: suspend (List<SongItem>) -> Unit, loadProgress: suspend (Int, Int) -> Unit) {
       queryAudioFiles(onLoaded, loadProgress)
    }

   private suspend fun queryAudioFiles(onLoaded: suspend (List<SongItem>) -> Unit, loadProgress: suspend (Int, Int) -> Unit) {
        val audioList = mutableListOf<SongItem>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val queryUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        context.contentResolver.query(
            queryUri,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
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
                loadProgress.invoke(++count, total)
            }
            onLoaded.invoke(audioList)
        }
    }
}