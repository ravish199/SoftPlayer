package com.ravish.softplayer.ui.customview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.R

@Composable
fun MyCustomSliderThumb(
    modifier: Modifier = Modifier, // Allow passing modifiers from the Slider's thumb lambda if needed
    interactionSource: MutableInteractionSource,
    thumbColor: Color = Color.Transparent,
    iconColor: Color = Color.Transparent,
    enabled: Boolean = true,
    baseSize: Dp = 20.dp,
    iconSize: Dp = 12.dp,
    pressedSizeIncrease: Dp = 4.dp, // How much bigger it gets when pressed
    baseElevation: Dp = 2.dp,
    iconElevation: Dp = 1.dp,
    pressedElevationIncrease: Dp = 4.dp
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentSize = if (isPressed) baseSize + pressedSizeIncrease else baseSize
    val currentElevation = if (isPressed) baseElevation + pressedElevationIncrease else baseElevation
    var iconCurrentElevation = if (isPressed) iconElevation + pressedElevationIncrease else iconElevation
    val currentThumbColor = if (enabled) thumbColor else thumbColor.copy(alpha = 0.6f)

    Box(
        modifier = modifier // Apply any external modifiers first
            .size(currentSize) // Control the thumb's size
            ,
        contentAlignment = Alignment.Center // Center the icon
    ) {
        Icon(
            painter = painterResource(R.drawable.circle),
            contentDescription = "Slider Thumb", // Accessibility
            tint = if (enabled) iconColor else iconColor.copy(alpha = 0.6f),
            modifier = Modifier.size(iconSize).shadow( // Apply shadow for an elevated look
                elevation = if (enabled) iconCurrentElevation else 0.dp,
                shape = CircleShape,
                clip = false // Important for shadow to be visible outside the clip
            ) // Scale icon with thumb size
        )
    }
}

@Preview(showBackground = false)
@Composable
fun CustomThumbPreview() {
    MaterialTheme {
        val interactionSource = remember { MutableInteractionSource() }
        var isEnabled by remember { mutableStateOf(true) }
        var isPressedManual by remember { mutableStateOf(false) } // For previewing pressed state

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(20.dp)) {
            Text("Custom Thumb (Interactive)")
            MyCustomSliderThumb(
                interactionSource = interactionSource,
                thumbColor = Color.White,
                iconColor = Color.White,
                enabled = isEnabled
            )

            Text("Manually Pressed State:")
            // Simulate pressing for preview
            LaunchedEffect(isPressedManual) {
                if (isPressedManual) {
                    interactionSource.tryEmit(androidx.compose.foundation.interaction.PressInteraction.Press(
                        Offset.Zero))
                } else {
                    interactionSource.tryEmit(androidx.compose.foundation.interaction.PressInteraction.Release(androidx.compose.foundation.interaction.PressInteraction.Press(
                        Offset.Zero)))
                }
            }
            MyCustomSliderThumb(
                interactionSource = interactionSource, // Same source to show effect
                thumbColor = Color.Cyan,
                iconColor = Color.Black,
                enabled = isEnabled
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Enabled")
                Switch(checked = isEnabled, onCheckedChange = { isEnabled = it })
                Spacer(Modifier.width(10.dp))
                Text("Force Press")
                Switch(checked = isPressedManual, onCheckedChange = {isPressedManual = it})
            }
        }
    }
}