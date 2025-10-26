package com.ravish.softplayer.ui.navigation// AppNavigation.kt (or within your MainActivity or a dedicated navigation file)

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

/**
 * Main composable function that sets up the navigation graph for the application.
 *
 * @param modifier Modifier for the NavHost.
 * @param navController The NavHostController to manage navigation. Defaults to a new remembered controller.
 * @param startDestination The route for the starting destination of the graph. Defaults to Home screen.
 */
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppNavGraph(viewModel: PlayerViewModel,
                modifier: Modifier = Modifier,
                navController: NavHostController = rememberNavController(),
                startDestination: String = Screen.SongLoadingScreen.route,

                ) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Song loading Screen
        composable(route = Screen.SongLoadingScreen.route) {
            SongLoadingScreen()
        }

        // Home Screen
        composable(route = Screen.PlayerMainScreen.route) {
            PlayerMainScreen(viewModel)
        }

        composable(route = Screen.TrackListScreen.route) {
            TrackListScreen(viewModel)
        }

        composable(route = Screen.EqualizerScreen.route) {
            EqualizerScreen(viewModel)
        }

    }
}

