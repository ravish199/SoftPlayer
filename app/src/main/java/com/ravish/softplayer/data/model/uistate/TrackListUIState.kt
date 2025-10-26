package com.ravish.softplayer.data.model.uistate

import kotlinx.coroutines.flow.StateFlow

data class TrackListUIState(
    val openTrackListStatus: StateFlow<Boolean>,
    val filterTrack: StateFlow<String>
    )