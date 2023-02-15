package com.ali.myapplication

import android.content.Context
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

object CashUtil {
    lateinit var downloadCache: Cache
    private var databaseProvider: DatabaseProvider? = null
    private var downloadDirectory: File? = null
    private const val CONTENT_DIRECTORY = "downloads"


    @Synchronized
    fun getDownloadCache(context: Context): Cache {
        val downloadContentDirectory =
            File(getDownloadDirectory(context), CONTENT_DIRECTORY)
        getDatabaseProvider(context)?.let {
            downloadCache = SimpleCache(
                downloadContentDirectory, NoOpCacheEvictor(), it
            )
        }
        return downloadCache
    }

    @Synchronized
   private fun getDatabaseProvider(context: Context): DatabaseProvider? {
        if (databaseProvider == null) {
            databaseProvider = StandaloneDatabaseProvider(context)
        }
        return databaseProvider
    }

    @Synchronized
    private fun getDownloadDirectory(context: Context): File? {
        if (downloadDirectory == null) {
            downloadDirectory = context.getExternalFilesDir(null)
            if (downloadDirectory == null) {
                downloadDirectory = context.filesDir
            }
        }
        return downloadDirectory
    }
}
