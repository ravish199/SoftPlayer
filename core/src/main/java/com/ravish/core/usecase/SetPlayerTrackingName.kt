package com.ravish.core.usecase

import com.ravish.core.PlayerConfigDataRepository

class SetPlayerTrackingName(private val playerConfigDataRepository: PlayerConfigDataRepository) {
    operator fun invoke(name: String) {
        playerConfigDataRepository.playingTrackName = name
    }
}