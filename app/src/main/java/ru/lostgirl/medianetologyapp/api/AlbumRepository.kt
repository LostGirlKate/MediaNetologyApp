package ru.lostgirl.medianetologyapp.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.lostgirl.medianetologyapp.model.Album
import java.util.concurrent.TimeUnit

class AlbumRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<Album>() {}

    companion object {
        private const val BASE_URL =
            "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/album.json"
        const val BASE_MEDIA_URL =
            "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
    }

    fun getAll(): Album {
        val request: Request = Request.Builder()
            .url(BASE_URL)
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }
}