package com.ravish.core.usecase

import com.ravish.core.PlayerConfigDataRepository

class SetShuffleOn(private val playerConfigDataRepository: PlayerConfigDataRepository) {
    operator fun invoke(value: Boolean) {
        playerConfigDataRepository.shuffleOn = value
    }
}