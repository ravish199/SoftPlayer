package com.ravish.softplayer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = false, widthDp = 400, heightDp = 700)
@Composable
fun PreviewComplexPlayerLayout() {
    MaterialTheme { // Ensure you have a MaterialTheme in your app
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            ComplexPlayerLayout()
        }
    }
}