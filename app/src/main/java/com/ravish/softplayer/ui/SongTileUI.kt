package com.ravish.softplayer.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ravish.softplayer.R
import com.ravish.player.data.model.SongItem
import com.ravish.softplayer.Utils
import com.ravish.softplayer.ui.theme.ButtonBackgroundColor
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay

var songListState = mutableStateOf(listOf<com.ravish.player.data.model.SongItem>())
var songUpdate = mutableStateOf<com.ravish.player.data.model.SongItem?>(null)

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("Recycle")
@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun DrawSongTileUI(viewModel: PlayerViewModel, modifier: Modifier) {

   with(viewModel.audioList.collectAsStateWithLifecycle()) {
       Log.d("DrawSongTileUI:", "songlist.size:${this.value.size}")


       if (this.value.isNotEmpty()) {
           Row(
               modifier = modifier,
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically,
           ) {
               val carouselState = rememberCarouselState(initialItem = 0) { this@with.value.size}

               with(viewModel.getPlayEndedState()?.collectAsStateWithLifecycle()) {

               }

               val snapAnimationSpec: AnimationSpec<Float> = spring(stiffness = Spring.StiffnessHigh)
               HorizontalMultiBrowseCarousel(
                   state = carouselState,
                   modifier = Modifier
                       .fillMaxWidth()
                       .fillMaxHeight(),
                   preferredItemWidth = 300.dp,
                   itemSpacing = 10.dp,
                   flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(state = carouselState,
                       snapAnimationSpec = snapAnimationSpec),
               ) { i ->
                   val song = this@with.value[i]
                   val bitmap =  Utils.getImage(LocalContext.current, song.contentUri)
                   viewModel.updateBackground(bitmap)
                   bitmap.asImageBitmap()?.let {
                       Card(
                           modifier = modifier.fillMaxSize().padding(10.dp),
                           elevation = CardDefaults.cardElevation(
                               defaultElevation = 10.dp

                           )
                       ) {
                           Image(
                               bitmap = it,
                               modifier = Modifier
                                   .fillMaxSize().clickable {
                                       updatePlayerSong(it.asAndroidBitmap(), song)
                                   },
                               contentDescription = "",
                               contentScale = ContentScale.FillBounds
                           )
                       }
                 }
               }
           }
       } else {

           Row(modifier = modifier) {
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
fun AddSongImage(modifier: Modifier, songItem: com.ravish.player.data.model.SongItem) {
    val imagePath = LocalContext.current.resources.openRawResource(R.raw.song_loading)
    AsyncImage(
        modifier = modifier,
        model = "res/raw/song_loading.png",
        contentDescription = "Image"
    )
}

fun updateSongList(songList: List<com.ravish.player.data.model.SongItem>) {
    songListState.value = songList
}



@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@Preview
fun DrawSongTileUIPreview() {
    //DrawSongTileUI(viewModel = viewModel(), modifier = Modifier)
    AddSongImage(modifier = Modifier.fillMaxSize(),
        songItem = SongItem(100L,
            "", "", "", 100L, "", Uri.parse("res/raw/song_loading.png")))
}



