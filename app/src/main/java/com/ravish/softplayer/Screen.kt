package com.ravish.softplayer

// AppDestinations.kt (can be a separate file or within your navigation setup file)
sealed class Screen(val route: String) {
    object PlayerMainScreen : Screen("player_main_screen")
    object SongLoadingScreen : Screen("song_loading_screen")
}