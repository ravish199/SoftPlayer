package com.ravish.softplayer.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ravish.softplayer.R
import com.ravish.softplayer.SongItem
import com.ravish.softplayer.Utils
import com.ravish.softplayer.ui.theme.SongsTileBackgroundColor
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.reflect.InvocationTargetException

var songListState = mutableStateOf(listOf<SongItem>())


@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("Recycle")
@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun DrawSongTileUI(viewModel: PlayerViewModel, modifier: Modifier) {

   with(viewModel.audioList.collectAsStateWithLifecycle()) {
       Log.d("DrawSongTileUI:", "songlist.size:${this.value.size}")
       if (this.value.isNotEmpty()) {
           Row(
               modifier = modifier.background(Color.Transparent),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically,
           ) {
               val carouselState = rememberCarouselState { this@with.value.size}
               HorizontalMultiBrowseCarousel(
                   state = carouselState,
                   modifier = Modifier
                       .fillMaxWidth()
                       .fillMaxHeight()
                       .padding(10.dp),
                   preferredItemWidth = 100.dp,
                   itemSpacing = 5.dp,
                   flingBehavior = CarouselDefaults.noSnapFlingBehavior(),
               ) { i ->
                   val song = this@with.value[i]
                   Log.d("DrawSongTileUI:", "contentUri:${song.contentUri.path}")
                   val bitmap =  Utils.getImage(LocalContext.current, song.contentUri)
                   viewModel.updateBackground(bitmap)
                   bitmap.asImageBitmap()?.let {
                     Image(
                         bitmap = it,
                         modifier = Modifier
                             .fillMaxHeight().clickable {
                              updateThumbImage(it.asAndroidBitmap())
                             }
                             .maskClip(MaterialTheme.shapes.extraLarge),
                         contentDescription = "",
                         contentScale = ContentScale.Fit
                     )
                 }
               }
           }
       } else {

           Row(modifier = modifier.background(color = Color.Black)) {
               Image(
                   modifier = Modifier
                       .padding(10.dp)
                       .fillMaxWidth()
                       .fillMaxHeight(),
                   painter = painterResource(R.drawable.empty_song_list),
                   contentDescription = "Song Name",
                   contentScale = ContentScale.Fit,
                   alpha = 0.5f
               )
           }
       }
   }
}

@Composable
fun AddSongImage(modifier: Modifier, songItem: SongItem) {
    val imagePath = LocalContext.current.resources.openRawResource(R.raw.song_loading)
    AsyncImage(
        modifier = modifier,
        model = "res/raw/song_loading.png",
        contentDescription = "Image"
    )
}

fun updateSongList(songList: List<SongItem>) {
    songListState.value = songList
}



/*
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@Preview
fun DrawSongTileUIPreview() {
    //DrawSongTileUI(viewModel = viewModel(), modifier = Modifier)
    AddSongImage(modifier = Modifier.fillMaxSize(),
        songItem = SongItem(100L,
            "", "", "", 100L, "", Uri.parse("res/raw/song_loading.png")))
}
*/



