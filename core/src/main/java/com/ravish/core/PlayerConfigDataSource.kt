package com.ravish.core

interface PlayerConfigDataSource {
    var playingTrackName:String
    var shuffleOn:Boolean
    var allRepeatOn:Boolean
    var repeatCurrentOn:Boolean
    var sleepTime:Int
    var playbackSpeed:Int
    var jumpToTime:Int
}