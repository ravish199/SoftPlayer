package com.ravish.softplayer.data.model

import kotlinx.coroutines.flow.StateFlow

data class SliderUIState(
    var totalDurationState:StateFlow<Long>,
    var currentPositionState:StateFlow<Long>
)