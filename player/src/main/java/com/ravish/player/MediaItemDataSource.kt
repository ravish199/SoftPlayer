package com.ravish.player

import com.ravish.player.data.model.SongItem

interface MediaItemDataSource {
    fun addMediaItemList(audioList: List<SongItem>)
    fun updateMediaItemIndex(index:Int)
}