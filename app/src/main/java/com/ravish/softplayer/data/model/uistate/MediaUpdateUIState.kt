package com.ravish.softplayer.data.model.uistate

import com.ravish.player.data.model.SongItem
import kotlinx.coroutines.flow.StateFlow

data class MediaUpdateUIState(
    val mediaItemsUpdateState:StateFlow<List<SongItem>>,
)