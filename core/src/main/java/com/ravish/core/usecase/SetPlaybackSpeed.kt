package com.ravish.core.usecase

import com.ravish.core.PlayerConfigDataRepository

class SetPlaybackSpeed(private val playerConfigDataRepository: PlayerConfigDataRepository) {
    operator fun invoke(value: Int) {
        playerConfigDataRepository.playbackSpeed = value
    }
}