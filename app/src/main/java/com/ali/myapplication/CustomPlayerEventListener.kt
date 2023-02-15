package com.ali.myapplication

import android.util.Log
import com.ali.myapplication.RemoteAnalyticsAPI.sendAnalyticsEvent
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player

class CustomPlayerEventListener : Player.Listener {
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_READY -> {
                sendAnalyticsEvent("1", 2, 0, "PlayBack")
            }
            Player.STATE_ENDED -> {
                sendAnalyticsEvent("1", 2, 0, "PlayBack")
            }
            Player.STATE_BUFFERING -> {
                sendAnalyticsEvent("3", 2, 5, "PlayBack")
            }
            Player.STATE_IDLE -> {
                TODO()
            }
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {}
    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}

    //handel playback errors
    override fun onPlayerError(error: PlaybackException) {
        when (error.errorCode) {
            PlaybackException.ERROR_CODE_UNSPECIFIED -> {
                Log.e("AliApp", "unspecified playback error ")
            }
            PlaybackException.ERROR_CODE_REMOTE_ERROR -> {
                Log.e("AliApp", "unspecified playback error ")
            }
            else -> {}
        }
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
}
