package com.ravish.softplayer.ui.navigation

// AppDestinations.kt (can be a separate file or within your navigation setup file)
sealed class Screen(val route: String) {
    object PlayerMainScreen : Screen("player_main_screen")
    object SongLoadingScreen : Screen("song_loading_screen")

    object TrackListScreen : Screen("track_list_screen")
}