package com.ravish.softplayer.ui

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ravish.softplayer.data.service.PlayerService
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PlayerMainScreen(viewModel: PlayerViewModel
) {
    Log.d("PlayerMainScreen:", "PlayerMainScreen")
   DrawPlayerUI(viewModel)

}

@Composable
fun MainScreen(playerService: PlayerService) {
    /*   with(playerService) {
           PlayerScreen(
               song = getCurrentSong().collectAsState(null).value,
               isPlaying = false,
               currentPositionMillis = getCurrentPosition()
                   .collectAsState(null).value!!,
               totalDurationMillis = getDuration().collectAsState(null).value!!,
               isShuffleOn = false,
               repeatMode = RepeatMode.OFF,
               onPlayPauseClick = {
                   if (getPlayer()?.isPlaying!!) {
                       getPlayer()!!.pause()
                   } else {
                       getPlayer()!!.play()
                   }
               },
               onNextClick = { getPlayer()?.next() },
               onPreviousClick = { getPlayer()?.previous() },
               onShuffleClick = {

               },
               onRepeatModeChange = {

               },
               onSeekBarPositionChange = { sliderPosition ->
                   getPlayer()?.seekTo(
                       (sliderPosition * getPlayer()?.getPlayer()?.duration!!).toLong()
                   )
               },
               modifier = Modifier.padding(10.dp)
           )
       }*/
}

