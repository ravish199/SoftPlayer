package com.ravish.core.usecase

import com.ravish.core.PlayerConfigDataRepository

class SetJumpToTime(private val playerConfigDataRepository: PlayerConfigDataRepository) {
    operator fun invoke(value: Int) {
        playerConfigDataRepository.jumpToTime = value
    }
}