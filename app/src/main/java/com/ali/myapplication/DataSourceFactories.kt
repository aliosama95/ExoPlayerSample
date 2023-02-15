package com.ali.myapplication

import android.content.Context
import com.ali.myapplication.CashUtil.getDownloadCache
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

object DataSourceFactories {

    private lateinit var dataSourceFactory: DataSource.Factory
    private const val USER_AGENT = "ExoPlayer-Agent"


    @Synchronized
    fun getHttpDataSourceFactory(context: Context): DataSource.Factory {

        return DefaultHttpDataSource.Factory().setUserAgent(USER_AGENT).setTransferListener(
            DefaultBandwidthMeter.Builder(context).setResetOnNetworkTypeChange(false).build()
        )
    }

    fun getRenderersFactory(
        context: Context, preferExtensionRenderer: Boolean
    ): RenderersFactory {
        val extensionRendererMode =
            if (preferExtensionRenderer) DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
        return DefaultRenderersFactory(context.applicationContext)
            .setExtensionRendererMode(extensionRendererMode)
    }


    @Synchronized
    fun getDataSourceFactory(context: Context): DataSource.Factory {
        if (!this::dataSourceFactory.isInitialized){

            val upstreamFactory: DefaultDataSource.Factory =
                DefaultDataSource.Factory(context, getHttpDataSourceFactory(context))
            dataSourceFactory = buildDataSource(upstreamFactory, getDownloadCache(context))
        
        }
        return dataSourceFactory
    }


    private fun buildDataSource(
        upstreamFactory: DataSource.Factory, cache: Cache
    ): CacheDataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setCacheWriteDataSinkFactory(null)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }
}
