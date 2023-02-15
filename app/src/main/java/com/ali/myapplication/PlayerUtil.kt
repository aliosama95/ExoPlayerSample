package com.ali.myapplication

import android.net.Uri
import androidx.core.net.toUri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaItem.SubtitleConfiguration
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.util.MimeTypes
import com.google.common.collect.ImmutableList

object PlayerUtil {
    private const val URL = "https://bitmovin-a.akamaihd.net/content/art-of-motion_drm/mpds/11331.mpd"
    private val drmSchemeUuid = C.WIDEVINE_UUID // DRM Type
    private const val DRM_LICENSE_URL = "https://test.com?provider=widevine_test"

    fun getLoadController(): DefaultLoadControl {
        val allocator = DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE)
        return DefaultLoadControl.Builder().setAllocator(allocator)
            .setBufferDurationsMs(8000 * 60, 12000 * 60, 2500, 5000).setBackBuffer(60 * 8000, true).build()
    }

    fun getExampleMediaItem(): MediaItem {
        return MediaItem.Builder().setUri(Uri.parse(URL)).setDrmConfiguration(
                MediaItem.DrmConfiguration.Builder(drmSchemeUuid).setLicenseUri(DRM_LICENSE_URL).build()
            ).setSubtitleConfigurations(ImmutableList.of(createSubtitleConfiguration("", "")))
            .setMimeType(MimeTypes.APPLICATION_MPD).setTag(null).build()
    }


    //subtitleURI should be passed to this method but since it is an example
    // should be updated when data source is available
    private fun createSubtitleConfiguration(
        mimeType: String, subtitleURI: String
    ): SubtitleConfiguration = SubtitleConfiguration.Builder(
        (subtitleURI).toUri()
    ).setMimeType(
            mimeType
        )
        // .setLanguage()
        .setSelectionFlags(C.SELECTION_FLAG_DEFAULT).build()

}

