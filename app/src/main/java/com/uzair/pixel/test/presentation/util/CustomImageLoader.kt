package com.uzair.pixel.test.presentation.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object ImageCache {
    private const val MAX_CACHE_SIZE = 50

    private val cache = object : LinkedHashMap<String, Bitmap>(MAX_CACHE_SIZE, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Bitmap>?): Boolean {
            return size > MAX_CACHE_SIZE
        }
    }

    fun get(url: String) = cache[url]

    fun put(url: String, bitmap: Bitmap) {
        cache[url] = bitmap
    }
}

suspend fun customImageLoader(url: String): Bitmap? {
    ImageCache.get(url)?.let { return it }

    return withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            val bitmap = BitmapFactory.decodeStream(connection.inputStream)
            connection.disconnect()

            bitmap?.also { ImageCache.put(url, it) }
        } catch (e: Exception) {
            null
        }
    }
}
