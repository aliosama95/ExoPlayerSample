package com.ali.myapplication

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.drm.FrameworkMediaDrm
import com.google.common.collect.ImmutableList

object DemoMediaItemProvider {


    private const val DRM_LICENSE_URL = "https://test.com?provider=widevine_test"
    private val DRM_SCHEME_UUID = C.WIDEVINE_UUID // DRM Type


    //this can be modified to return media items not a single item
    //should create medea items base on the data received from Intent
    // for the sake of example we will assume data is ready to be used
    fun provideMediaItems(intent: Intent): MediaItem? {

        val mediaItem = createMediaItem(intent.data)
        val drmConfiguration = mediaItem.localConfiguration?.drmConfiguration
        drmConfiguration?.let {
            if (!FrameworkMediaDrm.isCryptoSchemeSupported(drmConfiguration.scheme)) {
                Log.e("Ali_APP", "Unsupported Schema")

                return null
            }
        }
        return mediaItem
    }

    private fun createMediaItem(
        uri: Uri?
    ): MediaItem {
        val mimeType = ""
        val title = ""
        val adTagUri = ""
        val builder = MediaItem.Builder().setUri(uri).setMimeType(mimeType)
            .setMediaMetadata(MediaMetadata.Builder().setTitle(title).build())
        adTagUri?.let {
            builder.setAdsConfiguration(
                MediaItem.AdsConfiguration.Builder(Uri.parse(adTagUri)).build()
            )
        }
        createDRMConfiguration(builder)
        createSubtitleConfiguration(mimeType, "")?.let { builder.setSubtitleConfigurations(ImmutableList.of()) }
        return builder.build()
    }


    //subtitleURI should be passed to this method
    // should be updated when data source is available

    private fun createSubtitleConfiguration(
        mimeType: String, subtitleURI: String
    ): MediaItem.SubtitleConfiguration? = MediaItem.SubtitleConfiguration.Builder(
        (subtitleURI).toUri()
    ).setMimeType(
        mimeType
    )
        // .setLanguage()
        .setSelectionFlags(C.SELECTION_FLAG_DEFAULT).build()


    private fun createDRMConfiguration(
        builder: MediaItem.Builder
    ): MediaItem.Builder {

        val headers: MutableMap<String, String> =
            HashMap() // should have key value to fetch from passed data here is empty for the sake of example

        val drmUuid = DRM_SCHEME_UUID //should be replaced by passed data and use Util.getDrmUuid("DRM SCHEME") .

        builder.setDrmConfiguration(
            MediaItem.DrmConfiguration.Builder(drmUuid)
                .setLicenseUri(DRM_LICENSE_URL)
                .setMultiSession(false).setForceDefaultLicenseUri(
                    true
                ).setLicenseRequestHeaders(headers).build()
        )
        return builder
    }

}
