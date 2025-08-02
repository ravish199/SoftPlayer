package com.ravish.softplayer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ComplexPlayerLayout(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Create references for each Composable to constrain them
        val (
            thumbnail,
            leftButton1, leftButton2, leftButton3, leftButton4,
            rightButton1, rightButton2, rightButton3, rightButton4,
            bottomLeftButton1, bottomLeftButton2,
            bottomRightButton1, bottomRightButton2
        ) = createRefs()

        // --- Center Player Thumbnail ---
        PlayerThumbnail(
            modifier = Modifier
                .constrainAs(thumbnail) {
                    centerTo(parent) // Center horizontally and vertically
                    width = Dimension.percent(0.4f) // Example: 40% of parent width
                    // height will be determined by aspectRatio in PlayerThumbnail
                }
        )

        // --- Four Buttons on Front Left Side ---
        // These will be vertically chained and constrained to the left of the thumbnail
        SideButton(text = "L1", onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(leftButton1) {
                top.linkTo(thumbnail.top)
                end.linkTo(thumbnail.start, margin = 16.dp)
            }
        )
        SideButton(text = "L2", onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(leftButton2) {
                top.linkTo(leftButton1.bottom, margin = 8.dp)
                start.linkTo(leftButton1.start) // Align start with L1
            }
        )
        SideButton(text = "L3", onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(leftButton3) {
                top.linkTo(leftButton2.bottom, margin = 8.dp)
                start.linkTo(leftButton2.start)
            }
        )
        SideButton(text = "L4", onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(leftButton4) {
                top.linkTo(leftButton3.bottom, margin = 8.dp)
                start.linkTo(leftButton3.start)
                bottom.linkTo(thumbnail.bottom) // Optional: align bottom with thumbnail
            }
        )
        // Create a vertical chain for left buttons for even distribution (optional but good)
        createVerticalChain(
            leftButton1, leftButton2, leftButton3, leftButton4,
            chainStyle = androidx.constraintlayout.compose.ChainStyle.SpreadInside
        )


        // --- Four Buttons on Front Right Side ---
        SideButton(text = "R1", onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(rightButton1) {
                top.linkTo(thumbnail.top)
                start.linkTo(thumbnail.end, margin = 16.dp)
            }
        )
        SideButton(text = "R2", onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(rightButton2) {
                top.linkTo(rightButton1.bottom, margin = 8.dp)
                start.linkTo(rightButton1.start)
            }
        )
        SideButton(text = "R3", onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(rightButton3) {
                top.linkTo(rightButton2.bottom, margin = 8.dp)
                start.linkTo(rightButton2.start)
            }
        )
        SideButton(text = "R4", onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(rightButton4) {
                top.linkTo(rightButton3.bottom, margin = 8.dp)
                start.linkTo(rightButton3.start)
                bottom.linkTo(thumbnail.bottom) // Optional
            }
        )
        createVerticalChain(
            rightButton1, rightButton2, rightButton3, rightButton4,
            chainStyle = androidx.constraintlayout.compose.ChainStyle.SpreadInside
        )


        // --- Two Buttons on Bottom Left Side ---
        SideButton(text = "BL1", onClick = { /*TODO*/ }, isBottomButton = true,
            modifier = Modifier.constrainAs(bottomLeftButton1) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }
        )
        SideButton(text = "BL2", onClick = { /*TODO*/ }, isBottomButton = true,
            modifier = Modifier.constrainAs(bottomLeftButton2) {
                bottom.linkTo(parent.bottom)
                start.linkTo(bottomLeftButton1.end, margin = 8.dp)
            }
        )

        // --- Two Buttons on Bottom Right Side ---
        SideButton(text = "BR1", onClick = { /*TODO*/ }, isBottomButton = true,
            modifier = Modifier.constrainAs(bottomRightButton1) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        )
        SideButton(text = "BR2", onClick = { /*TODO*/ }, isBottomButton = true,
            modifier = Modifier.constrainAs(bottomRightButton2) {
                bottom.linkTo(parent.bottom)
                end.linkTo(bottomRightButton1.start, margin = 8.dp)
            }
        )
    }
}

