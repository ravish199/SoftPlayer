package com.ravish.softplayer.ui.customview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.ui.theme.ActiveTrackColor
import com.ravish.softplayer.ui.theme.ButtonContainerColorSemiTransparent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPlayerTrack(
    sliderState: SliderState,
    activeTrackColor: Color,
    inactiveTrackColor: Color,
    desiredTrackHeight: Dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(desiredTrackHeight)
            .padding(top = paddingTop, bottom = paddingBottom)// Set the canvas height
    ) {
        val isRtl = layoutDirection == androidx.compose.ui.unit.LayoutDirection.Rtl
        val sliderStart = Offset(0f, center.y)
        val sliderEnd = Offset(size.width, center.y)

        // Ensure sliderState.value is used correctly (it's normalized 0-1)
        val thumbFraction = (size.width / sliderState.valueRange.endInclusive)
        val activeTrackPx = thumbFraction * sliderState.value

        val trackStrokeWidth = desiredTrackHeight.toPx() // Use the desired height for stroke

        // Draw Inactive Track
        drawLine(
            color = inactiveTrackColor,
            strokeWidth = trackStrokeWidth,
            cap = StrokeCap.Round,
            start = sliderStart,
            end = sliderEnd
        )

        // Draw Active Track
        if (thumbFraction > 0f) {
            drawLine(
                color = activeTrackColor,
                strokeWidth = trackStrokeWidth,
                cap = StrokeCap.Round,
                start = sliderStart,
                end = Offset(
                    activeTrackPx,
                    center.y
                )
            )
        }
    }
}
