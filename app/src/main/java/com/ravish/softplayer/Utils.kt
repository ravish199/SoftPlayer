package com.ravish.softplayer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class Utils {
    companion object {
       val ioScope = CoroutineScope(Dispatchers.IO)
    }
}