package ru.lostgirl.medianetologyapp.model

import ru.lostgirl.medianetologyapp.presentation.uimodel.TrackUiModel

data class Track(
    val id: Int,
    val file: String,
)

fun Track.toUIModel(albumName: String) =
    TrackUiModel(
        id = id,
        file = file,
        albumName = albumName
    )
