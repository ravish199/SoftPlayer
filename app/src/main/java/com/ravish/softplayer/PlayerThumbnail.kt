package com.ravish.softplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// import com.yourpackage.R // For your placeholder drawable

@Composable
fun PlayerThumbnail(modifier: Modifier = Modifier) {
    // In a real app, you'd load an actual image
    Image(
        painter = painterResource(R.drawable.music),
        contentDescription = "Player Thumbnail",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f) // Keep it square
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
fun SideButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isBottomButton: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(width = if (isBottomButton) 80.dp else 60.dp, height = 60.dp),
        shape = if (isBottomButton) RoundedCornerShape(8.dp) else CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Text(text, fontSize = if (isBottomButton) 12.sp else 10.sp)
    }
}

