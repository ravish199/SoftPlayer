package com.ravish.core.usecase

import com.ravish.core.PlayerConfigDataRepository

class SetAllRepeatOn(private val playerConfigDataRepository: PlayerConfigDataRepository) {
    operator fun invoke(value: Boolean) {
        playerConfigDataRepository.allRepeatOn = value
    }
}