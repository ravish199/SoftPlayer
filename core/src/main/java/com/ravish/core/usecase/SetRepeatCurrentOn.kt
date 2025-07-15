package com.ravish.core.usecase

import com.ravish.core.PlayerConfigDataRepository

class SetRepeatCurrentOn(private val playerConfigDataRepository: PlayerConfigDataRepository) {
    operator fun invoke(value: Boolean) {
        playerConfigDataRepository.repeatCurrentOn = value
    }
}