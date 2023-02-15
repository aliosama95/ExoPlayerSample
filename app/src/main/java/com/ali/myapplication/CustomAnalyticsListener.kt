package com.ali.myapplication

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.exoplayer2.analytics.AnalyticsListener

class CustomPlayerAnalyticsListener : AnalyticsListener {

    override fun onDroppedVideoFrames(eventTime: AnalyticsListener.EventTime, droppedFrames: Int, elapsedMs: Long) {
        super.onDroppedVideoFrames(eventTime, droppedFrames, elapsedMs)

        Log.w(TAG, "Dropped $droppedFrames frames in the last $elapsedMs ms")

        // Send to Remote Analytics
        //  RemoteAnalyticsAPI.sendAnalyticsEvent(2,eventTime.currentPlaybackPositionMs , elapsedMs ,"PlaybackEvent" )

        //Check Connectivity / network and show message to user

        // lower bitrate
    }
}
