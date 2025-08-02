package com.ravish.softplayer// AppNavigation.kt (or within your MainActivity or a dedicated navigation file)

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ravish.softplayer.data.service.PlayerService
import com.ravish.softplayer.ui.SongLoadingScreen

/**
 * Main composable function that sets up the navigation graph for the application.
 *
 * @param modifier Modifier for the NavHost.
 * @param navController The NavHostController to manage navigation. Defaults to a new remembered controller.
 * @param startDestination The route for the starting destination of the graph. Defaults to Home screen.
 */
@Composable
fun AppNavGraph(playerService: PlayerService?,
                modifier: Modifier = Modifier,
                navController: NavHostController = rememberNavController(),
                startDestination: String = Screen.PlayerMainScreen.route,

                ) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // Home Screen
 /*       composable(route = Screen.PlayerMainScreen.route) {
            PlayerMainScreen(
                playerService = playerService,
                navigateToProfile = {
                    navController.navigate(Screen.SongLoadingScreen.route)
                }
            )
        }*/

        // Profile Screen
        composable(route = Screen.SongLoadingScreen.route) {
            SongLoadingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

