package com.ravish.softplayer.data.model

import com.ravish.player.data.model.SongItem
import kotlinx.coroutines.flow.StateFlow

data class MediaUpdateUIState(
    var mediaItemsUpdateState:StateFlow<List<SongItem>>,
)