package ru.lostgirl.medianetologyapp.model

import ru.lostgirl.medianetologyapp.presentation.uimodel.AlbumUIModel

data class Album(
    val id: Int = 0,
    val title: String = "",
    val subtitle: String = "",
    val artist: String = "",
    val published: String = "",
    val genre: String = "",
    val tracks: List<Track> = emptyList(),
)

fun Album.toUIModel() =
    AlbumUIModel(
        id = id,
        title = title,
        subtitle = subtitle,
        artist = artist,
        published = published,
        genre = genre,
        tracks = tracks.map { track ->
            track.toUIModel(albumName = title)
        }
    )