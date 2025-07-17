package com.ravish.softplayer

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.ravish.softplayer.ui.theme.SoftPlayerTheme

class MainActivity : ComponentActivity() {

    private var mediaFileManager: MediaFileManager? = null
    private var songList:List<SongItem>? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                loadAudioFiles()
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
                loadAudioFiles()
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

    private fun loadAudioFiles() {
        // Implementation in the next step
        // For now, let's just log or show a toast
        android.widget.Toast.makeText(
            this,
            "Permission granted. Loading audio...",
            android.widget.Toast.LENGTH_SHORT
        ).show()
        songList = mediaFileManager?.queryAudioFiles()
        // Do something with audioList, e.g., display in a RecyclerView
        songList?.forEach { audioFile ->
            android.util.Log.d("AudioFiles", "Title: ${audioFile.title}, Path: ${audioFile.data}")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mediaFileManager = MediaFileManager(this)
        checkPermissionAndLoadAudio()
        setContent {
            SoftPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    innerPadding.toString()
                  SongListScreen(songList!!, onSongClick = {

                  })
                }
            }
        }

        // Example: Trigger loading audio on a button click or when needed
        /*  val loadAudioButton: android.widget.Button =
              findViewById(R.id.loadAudioButton) // Assuming you have a button
          loadAudioButton.setOnClickListener {
              checkPermissionAndLoadAudio()
          }*/
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