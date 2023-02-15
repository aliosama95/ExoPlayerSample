package com.ali.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ali.myapplication.DemoMediaItemProvider.provideMediaItems
import com.ali.myapplication.PlayerUtil.getLoadController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.drm.DefaultDrmSessionManagerProvider
import com.google.android.exoplayer2.ext.ima.ImaServerSideAdInsertionMediaSource.AdsLoader
import com.google.android.exoplayer2.ext.ima.ImaServerSideAdInsertionMediaSource.Factory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters
import com.google.android.exoplayer2.util.EventLogger


open class MainActivity : AppCompatActivity() {
    private val AdsLoaderState: AdsLoader.State? = null
    private lateinit var AdLoader: AdsLoader
    private lateinit var player: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setView()
        initPlayer()
    }

    private fun setView() {
        val view = null
    }


    private fun initPlayer() {
        val mediaItem = provideMediaItems(intent)

        mediaItem?.let {
            player = ExoPlayer.Builder(this).setMediaSourceFactory(createMediaSourceFactory())
                .setLoadControl(getLoadController())
                .setRenderersFactory(DataSourceFactories.getRenderersFactory(this, false))
                .build()
            player.trackSelectionParameters = TrackSelectionParameters.Builder(this).build()
            player.setAudioAttributes(AudioAttributes.DEFAULT, true)
            player.addListener(CustomPlayerEventListener())
            player.addAnalyticsListener(CustomPlayerAnalyticsListener())
            player.addAnalyticsListener(EventLogger())
            AdLoader.setPlayer(player)
            player.setMediaItem(mediaItem)
        }
    }

    private fun createMediaSourceFactory(): MediaSource.Factory {
        val dataSourceFactory = DataSourceFactories.getDataSourceFactory(this)
        val drmSessionManagerProvider = DefaultDrmSessionManagerProvider()
        drmSessionManagerProvider.setDrmHttpDataSourceFactory(
            DataSourceFactories.getHttpDataSourceFactory(this)
        )
        return DefaultMediaSourceFactory(this).setDataSourceFactory(dataSourceFactory)
            .setDrmSessionManagerProvider(drmSessionManagerProvider)
            .setServerSideAdInsertionMediaSourceFactory(getAdMediaSourceFactory())
    }

    private fun getAdMediaSourceFactory(): Factory {
        val AdLoaderBuilder = AdsLoader.Builder(this, findViewById(R.id.player))
        AdsLoaderState?.let { AdLoaderBuilder.setAdsLoaderState(AdsLoaderState) }
        AdLoader = AdLoaderBuilder.build()
        return Factory(
            AdLoader,
            DefaultMediaSourceFactory(this).setDataSourceFactory(DataSourceFactories.getDataSourceFactory(this))
        )
    }


    protected fun releasePlayer() {
        player.let {
            player.release()

        }
    }

    override fun onPause() {
        super.onPause()
    }
}
