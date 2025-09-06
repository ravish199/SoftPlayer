package com.ravish.softplayer.data.model

import kotlinx.coroutines.flow.StateFlow

data class SongCategoryUIState(
    val categoryNameState:StateFlow<String>,
    val totalCountState:StateFlow<Int>,
    val songIndexState:StateFlow<Int>,
)