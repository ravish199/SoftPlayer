package com.ravish.softplayer

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
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.ravish.softplayer.data.service.PlayerService
import com.ravish.softplayer.ui.theme.SoftPlayerTheme
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Serializable
    object PlayerMainScreen

    @Serializable
    object SongLoadingScreen

    private var currentSongList: List<SongItem>? = null // Assuming you have a SongItem class
    private var currentSongIdex = 0

    @Inject
    lateinit var viewModel: PlayerViewModel

    private var isShuffle = false
    private var playerService: PlayerService? = null
    private var service by mutableStateOf(playerService)
    private var serviceConnection: ServiceConnection? = null
    var isLoading by mutableStateOf(true) // Simulate some initial loading

    /*    private val navController: NavHostController
            @Composable
            get() = rememberNavController()*/


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SoftPlayerTheme {

                if (isLoading) {
                    Log.d("connectService:", "Loading")
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.music),
                            contentDescription = ""
                        )
                        Text(
                            "Loading app...",
                            fontSize = 30.sp,
                            color = Color.Yellow
                        ) // This will likely be hidden by the splash screen
                        // if setKeepOnScreenCondition is working
                    }
                    connectService()
                } else {
                    service?.let {
                        navigateToMainScreen()
                    }

                }
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
    fun navigateToMainScreen() {
        Log.d("navigateToMainScreen:", "navigateToMainScreen")
        val navigationController = rememberNavController()
        AppNavGraph(service, navController = navigationController)
        navigationController.navigate(Screen.PlayerMainScreen.route)
    }

    override fun onStop() {
        super.onStop()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun connectService() {
        Log.d("connectService:", "connectService")
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                playerService = (p1 as PlayerService.ServiceBinder).getService()
                playerService?.let {
                    viewModel.initService(it)
                    checkPermissionAndLoadAudio()
                }
                Log.d("connectService:", "onServiceConnected")
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
        serviceConnection = null
        playerService = null
    }

    override fun onDestroy() {
        super.onDestroy()
   /*     if (!playerService?.isPlaying()!!) {
            disconnectService()
        }*/
    }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
             /*   view {
                    Log.d("connectService:", "onLoaded")
                    isLoading = false
                }*/
            } else {
                Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkPermissionAndLoadAudio() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_AUDIO
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
           /*     viewModel?.loadAudioFiles() {
                    Log.d("connectService:", "onLoaded")
                    isLoading = false
                }*/
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