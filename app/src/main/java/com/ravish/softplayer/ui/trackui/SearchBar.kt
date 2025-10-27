package com.ravish.softplayer.ui.trackui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravish.softplayer.R
import com.ravish.softplayer.Utils.drawShadow
import com.ravish.softplayer.data.model.ShadowType
import com.ravish.softplayer.ui.AddIcon2
import com.ravish.softplayer.ui.FakePlayerViewModel
import com.ravish.softplayer.ui.viewmodel.PlayerViewModel

@Composable
fun SearchBar(viewModel: PlayerViewModel, modifier: Modifier) {
    var searchClickStatus by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.drawShadow(
            shadowType = ShadowType.BOTTOM,
            shadowWidth = 5.dp
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AddIcon2(
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 8.dp, bottom = 8.dp),
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
                modifier = Modifier
                    .wrapContentSize()
                    .align(alignment = Alignment.CenterVertically),
                onClick = {
                    searchClickStatus = true
                },
                icon = R.drawable.icon_search
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun SearchBar_Preview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color.White)
    ) {
        SearchBar(
            viewModel = FakePlayerViewModel(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(alignment = Alignment.Center)
        )
    }
}