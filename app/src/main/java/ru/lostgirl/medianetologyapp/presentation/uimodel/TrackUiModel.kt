package ru.lostgirl.medianetologyapp.presentation.uimodel

data class TrackUiModel(
    val id: Int,
    val file: String,
    val albumName: String,
    val mediaState: MediaState = MediaState.STOP,
)
