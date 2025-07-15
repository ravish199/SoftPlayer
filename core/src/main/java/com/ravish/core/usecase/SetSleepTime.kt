package com.ravish.core.usecase

import com.ravish.core.PlayerConfigDataRepository

class SetSleepTime(private val playerConfigDataRepository: PlayerConfigDataRepository) {
    operator fun invoke(value: Int) {
        playerConfigDataRepository.sleepTime = value
    }
}