package com.ravish.softplayer.ui

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
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ravish.player.MediaFileManager
import com.ravish.player.MusicPlayer
import com.ravish.softplayer.ui.navigation.AppNavGraph
import com.ravish.softplayer.R
import com.ravish.softplayer.data.EqualizerSettingsManager
import com.ravish.softplayer.ui.navigation.Screen
import com.ravish.softplayer.data.service.PlayerService
import com.ravish.softplayer.ui.theme.SoftPlayerTheme
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Serializable
    object PlayerMainScreen

    @Serializable
    object SongLoadingScreen

    private var currentSongList: List<com.ravish.player.data.model.SongItem>? =
        null // Assuming you have a SongItem class
    private var currentSongIdex = 0


    private val viewModel: PlayerViewModel by viewModels()
    private lateinit var navigationController: NavHostController
    var userPermissionGranted = mutableStateOf(false)

    private var isShuffle = false
    private var playerService: PlayerService? = null
    private var serviceConnection: ServiceConnection? = null
    var isLoading = mutableStateOf(true) // Simulate some initial loading
    var songList: List<com.ravish.player.data.model.SongItem>? = null

    var loadMainScreenState = mutableStateOf(false)

    /*    private val navController: NavHostController
            @Composable
            get() = rememberNavController()*/


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        connectService()


        setContent {
            SoftPlayerTheme {
                val mainScreenLoader by loadMainScreenState
                navigationController = rememberNavController()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppNavGraph(viewModel, navController = navigationController)
                }
                DrawLoadingScreen()
                if (mainScreenLoader) {
                    Log.d("connectService:", "NavigateToMainScreen")
                    NavigateToMainScreen()
                }





                /*      with(viewModel.songLoadProgressUiState.collectAsStateWithLifecycle()) {
                          val progress = value.first / (value.second.toFloat())
                          Log.d("Progress:", "Progress: ${progress}")
                          Log.d("Progress:", "Count: ${value.first}, TOtal:${value.second}")
                          setProgress(progress, value.first)
                      }

                      with(viewModel.backgroundState.collectAsStateWithLifecycle()) {
                          // updateBackground(this.value)
                      }*/

                /* Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                     innerPadding.toString()
                     *//*    SongListScreen(songList!!, onSongClick = {

                        })*//*
                    //loadPlayerUI()
                    initNavigation()
                }*/
            }
        }

        // Example: Trigger loading audio on a button click or when needed
        /*  val loadAudioButton: android.widget.Button =
              findViewById(R.id.loadAudioButton) // Assuming you have a button
          loadAudioButton.setOnClickListener {
              checkPermissionAndLoadAudio()
          }*/
    }

    @Composable
    fun DrawLoadingScreen() {
        val loadingState by isLoading
        if (loadingState) {
            Log.d("connectService:", "Loading")
            navigateToSongLoadingScreen()
        } else {
            Log.d("navigateToMainScreen:", "navigateToMainScreen")
            /* with(viewModel.audioList.collectAsStateWithLifecycle()) {
                 navigateToMainScreen(this.value)
             }*/
            //  NavigateToMainScreen()
            //updateSongs(viewModel.audioList)
        }
    }

    @Composable
    fun navigateToSongLoadingScreen() {
        Log.d("navigateToSongLoadingScreen:", "navigateToSongLoadingScreen")
        navigationController.navigate(Screen.SongLoadingScreen.route)
    }

    @Composable
    fun NavigateToMainScreen() {
        Log.d("navigateToMainScreen:", "navigateToMainScreen")
        navigationController.navigate(Screen.PlayerMainScreen.route) {
            popUpTo(Screen.SongLoadingScreen.route) {
                inclusive = true
            }
        }
    }


    override fun onStop() {
        super.onStop()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun connectService() {
        Log.d("connectService:", "connectService")
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                Log.d("connectService:", "onServiceConnected")
                playerService = (p1 as PlayerService.ServiceBinder).getService()
                playerService?.let {
                    viewModel.initMusicPlayer(
                        musicPlayer = playerService?.musicPlayer,
                        audioEffectManager = playerService?.audioEffectManager,
                        equalizerSettingsManager = EqualizerSettingsManager(this@MainActivity)
                    )
                    checkPermissionAndLoadAudio(
                        playerService?.musicPlayer,
                        playerService?.mediaFileManager
                    )
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

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
               userPermissionGranted.value = true
            } else {
                Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkPermissionAndLoadAudio(
        musicPlayer: MusicPlayer?,
        fileManager: MediaFileManager?
    ) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_AUDIO
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if(userPermissionGranted.value) {
            viewModel.loadAudioFiles(musicPlayer, fileManager)
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.loadAudioFiles(musicPlayer, fileManager)
            }

            shouldShowRequestPermissionRationale(permission) -> {
                // Show an explanation to the user *asynchronously*
                // R.string.permission_rationale
                // After showing the rationale, request the permission again.
                // For simplicity here, we'll just request. In a real app, show UI.
                requestPermissionLauncher.launch(permission)
            }

            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }


    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SoftPlayerTheme {
        Greeting("Android")
    }
}