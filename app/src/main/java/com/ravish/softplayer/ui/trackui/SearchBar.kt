package com.ravish.softplayer.ui.trackui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.R
import com.ravish.softplayer.ui.AddIcon2
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun SearchBar(viewModel: PlayerViewModel, modifier: Modifier) {
    var searchClickStatus by remember { mutableStateOf(false) }

    Card(modifier = modifier.background(color=Color.White), shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White,
            )
    ) {
    Row(modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween) {
        AddIcon2(
            modifier = Modifier.wrapContentSize().padding(8.dp),
            onClick = {
                viewModel.closeTrackList()
                viewModel.filterTrackList("")
            },
            icon = R.drawable.icon_back_arrow
        )

        if (searchClickStatus) {
            SearchBarUI(
                viewModel = viewModel,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .weight(5f)
            ) {}

        } else {
            AddIcon2(
                modifier = Modifier.wrapContentSize().align(alignment = Alignment.CenterVertically),
                onClick = {
                    searchClickStatus = true
                },
                icon = R.drawable.icon_search
            )
        }
    }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun SearchBar_Preview() {
    SearchBar(viewModel = FakePlayerViewModel(), modifier = Modifier.fillMaxWidth().height(50.dp))
}