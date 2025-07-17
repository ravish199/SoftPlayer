package com.ravish.softplayer

import android.net.Uri
import androidx.compose.animation.core.RepeatMode
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
// import com.yourpackage.R // For placeholder drawable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    song: SongItem?,
    isPlaying: Boolean,
    currentPositionMillis: Long,
    totalDurationMillis: Long,
    isShuffleOn: Boolean,
    repeatMode: RepeatMode,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatModeChange: () -> Unit,
    onSeekBarPositionChange: (Float) -> Unit, // Value from 0.0 to 1.0
    modifier: Modifier = Modifier
) {
    // Determine a background color, possibly derived from album art in a real app
    val dominantColor = remember(song?.contentUri) {
        // In a real app, you might extract dominant color from album art
        // For now, use a fallback
        Color.DarkGray
    }
    val surfaceColor = MaterialTheme.colorScheme.surface
    val backgroundColor = remember(dominantColor) {
        dominantColor.copy(alpha = 0.6f).compositeOver(surfaceColor)
    }


    Surface(
        modifier = modifier.fillMaxSize(),
        color = backgroundColor // Apply a dynamic background
    ) {
        if (song == null) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("No song selected", style = MaterialTheme.typography.headlineSmall)
            }
            return@Surface
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround // Distribute space
        ) {
            // Album Art Section (Takes more space)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Takes available vertical space
                contentAlignment = Alignment.Center
            ) {
                Image(
                   painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "${song.title} album art",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f) // Makes it a square
                        .fillMaxSize(0.8f) // Use 80% of available space
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            RoundedCornerShape(16.dp)
                        )
                )
            }

            // Song Info Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = song.artist ?: "Unknown Artist",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Seek Bar and Time Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = if (totalDurationMillis > 0) currentPositionMillis.toFloat() / totalDurationMillis else 0f,
                    onValueChange = { newPositionFraction ->
                        onSeekBarPositionChange(newPositionFraction)
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatDuration(currentPositionMillis),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = formatDuration(totalDurationMillis),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // Controls Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerIconButton(
                    onClick = onShuffleClick,
                    icon = ImageVector.vectorResource(R.drawable.shuffle_icon),
                    contentDescription = "Shuffle",
                    tint = if (isShuffleOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )

                PlayerIconButton(
                    onClick = onPreviousClick,
                    icon = Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    iconSize = 36.dp // Make main controls slightly larger
                )

                PlayPauseButton(isPlaying = isPlaying, onClick = onPlayPauseClick)

                PlayerIconButton(
                    onClick = onNextClick,
                    icon = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    iconSize = 36.dp
                )

                val repeatIcon = when (repeatMode) {
                    RepeatMode.OFF -> Icons.Default.Repeat
                    RepeatMode.ONE -> Icons.Default.RepeatOne
                    RepeatMode.ALL -> Icons.Default.Repeat // Or a filled repeat icon if available
                }
                PlayerIconButton(
                    onClick = onRepeatModeChange,
                    icon = repeatIcon,
                    contentDescription = "Repeat: $repeatMode",
                    tint = if (repeatMode != RepeatMode.OFF) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun PlayerIconButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    iconSize: androidx.compose.ui.unit.Dp = 28.dp
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier.size(72.dp), // Larger central button
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(40.dp)
        )
    }
}

