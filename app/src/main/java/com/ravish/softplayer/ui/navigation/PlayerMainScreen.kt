package com.ravish.softplayer.ui.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.ravish.softplayer.ui.DrawPlayerUI
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PlayerMainScreen(viewModel: PlayerViewModel
) {
    Log.d("PlayerMainScreen:", "PlayerMainScreen")
    DrawPlayerUI(viewModel)

}


