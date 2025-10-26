package com.ravish.softplayer.ui

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ravish.softplayer.R
import com.ravish.softplayer.data.EqualizerSettingsManager
import com.ravish.softplayer.data.service.PlayerService
import com.ravish.softplayer.ui.navigation.AppNavGraph
import com.ravish.softplayer.ui.navigation.Screen
import com.ravish.softplayer.ui.theme.SoftPlayerTheme
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var navigationController: NavHostController
    private var playerService: PlayerService? = null
    private var serviceConnection: ServiceConnection? = null

    // 1. FIX: Store the callback as a property
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private var loadMainScreenState = mutableStateOf(false)

    private val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.POST_NOTIFICATIONS)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val requestMultiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.values.all { it }
            if (allPermissionsGranted) {
                Log.d("Permissions", "All permissions granted.")
                viewModel.loadAudioFiles(
                    playerService?.musicPlayer,
                    playerService?.mediaFileManager
                )
            } else {
                Log.d("Permissions", "One or more permissions were denied.")
                Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        connectService()

        // 2. FIX: Initialize the callback and add it
        onBackPressedCallback = object : OnBackPressedCallback(true) { // Start as disabled
            override fun handleOnBackPressed() {
                // This logic will only run when the callback is enabled
                Log.d("OnBackPressed", "Track list is open, closing it.")
                viewModel.closeTrackList()
                if (navigationController.currentDestination?.route == Screen.TrackListScreen.route) {
                    navigationController.popBackStack()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


        setContent {
            SoftPlayerTheme {
                navigationController = rememberNavController()
                AppNavGraph(viewModel, navController = navigationController)

                val mainScreenLoader by loadMainScreenState
                if (mainScreenLoader) {
                    NavigateToMainScreen()
                }

                // 3. FIX: Update the 'isEnabled' state of our stored callback
                val currentBackStackEntry by navigationController.currentBackStackEntryAsState()
                val isTrackScreen = currentBackStackEntry?.destination?.route == Screen.TrackListScreen.route

                // Update the callback's enabled state based on the current screen
                onBackPressedCallback.isEnabled = isTrackScreen
            }
        }
    }

    @Composable
    fun NavigateToMainScreen() {
        // This navigates to the main screen once the service is connected
        LaunchedEffect(Unit) {
            Log.d("navigateToMainScreen:", "Navigating to PlayerMainScreen")
            navigationController.navigate(Screen.PlayerMainScreen.route) {
                popUpTo(navigationController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        // This observes the state to open the track list screen
        val openTrackScreen by viewModel.trackListUIState.openTrackListStatus.collectAsStateWithLifecycle()

        LaunchedEffect(openTrackScreen) {
            if (openTrackScreen && navigationController.currentDestination?.route != Screen.TrackListScreen.route) {
                Log.d("NavigateToMainScreen:", "Navigating to TrackListScreen")
                navigationController.navigate(Screen.TrackListScreen.route) {
                    launchSingleTop = true
                }
            }
        }
    }

    private fun connectService() {
        Log.d("connectService:", "connectService")
        serviceConnection = object : ServiceConnection {
            @OptIn(UnstableApi::class)
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                Log.d("connectService:", "onServiceConnected")
                playerService = (p1 as PlayerService.ServiceBinder).getService()
                playerService?.let {
                    viewModel.initMusicPlayer(
                        musicPlayer = playerService?.musicPlayer,
                        audioEffectManager = playerService?.audioEffectManager,
                        equalizerSettingsManager = EqualizerSettingsManager(this@MainActivity)
                    )
                    checkAndRequestPermissions()
                    loadMainScreenState.value = true
                }

                Log.d("connectService:", "$playerService")
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                playerService = null
                Log.d("connectService:", "onServiceDisconnected")
            }
        }
        with(Intent(this, PlayerService::class.java)) {
            startForegroundService(this)
            bindService(
                this,
                serviceConnection!!,
                BIND_AUTO_CREATE
            )
        }
    }

    private fun disconnectService() {
        serviceConnection?.let {
            unbindService(it)
        }
        serviceConnection = null
        playerService = null
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectService()
    }

    private fun checkAndRequestPermissions() {
        val allPermissionsGranted = requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
        if (allPermissionsGranted) {
            Log.d("Permissions", "All permissions are already granted.")
            viewModel.loadAudioFiles(playerService?.musicPlayer, playerService?.mediaFileManager)
        } else {
            Log.d("Permissions", "Requesting permissions...")
            requestMultiplePermissionsLauncher.launch(requiredPermissions)
        }
    }
}
