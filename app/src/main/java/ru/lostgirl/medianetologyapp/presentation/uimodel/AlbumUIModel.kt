package ru.lostgirl.medianetologyapp.presentation.uimodel

data class AlbumUIModel(
    val id: Int = 0,
    val title: String = "",
    val subtitle: String = "",
    val artist: String = "",
    val published: String = "",
    val genre: String = "",
    val tracks: List<TrackUiModel> = emptyList(),
    val activeTrack: TrackUiModel? = null,
    val mediaState: MediaState = MediaState.STOP
)
