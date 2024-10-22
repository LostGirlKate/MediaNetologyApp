package ru.lostgirl.medianetologyapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.lostgirl.medianetologyapp.api.AlbumRepository
import ru.lostgirl.medianetologyapp.model.toUIModel
import ru.lostgirl.medianetologyapp.presentation.uimodel.MainState
import ru.lostgirl.medianetologyapp.presentation.uimodel.MediaState
import ru.lostgirl.medianetologyapp.presentation.uimodel.TrackUiModel

class MainViewModel : ViewModel() {

    private val repository: AlbumRepository = AlbumRepository()
    private val _mainState = MutableLiveData(MainState())
    val mainState: LiveData<MainState>
        get() = _mainState

    init {
        loadAll()
    }

    private fun loadAll() = viewModelScope.launch {
        val album = async(Dispatchers.IO) { repository.getAll() }.await()
        _mainState.value = MainState(album = album.toUIModel())
    }

    fun setActiveTrack(track: TrackUiModel) {
        setTrackGlobalState(track, MediaState.PLAY)
    }

    fun setPause(track: TrackUiModel) {
        setTrackGlobalState(track, MediaState.PAUSE)
    }

    private fun setTrackGlobalState(track: TrackUiModel, state: MediaState) {
        _mainState.value =
            _mainState.value?.copy(
                album = _mainState.value!!.album.copy(
                    activeTrack = track,
                    mediaState = state,
                    tracks = _mainState.value!!.album.tracks.map {
                        if (it.id == track.id) {
                            it.copy(mediaState = state)
                        } else {
                            it.copy(mediaState = MediaState.STOP)
                        }
                    }
                )
            )
    }

    fun getNext(): TrackUiModel {
        val activeTrack = _mainState.value!!.album.activeTrack
        var activeIndex = 0
        _mainState.value!!.album.tracks.forEachIndexed { index, track ->
            if (activeTrack != null) {
                if (track.id == activeTrack.id) {
                    activeIndex = index
                }
            }
        }
        val nextIndex =
            if (activeIndex == _mainState.value!!.album.tracks.size - 1) 0 else activeIndex + 1
        val nextTrack = _mainState.value!!.album.tracks[nextIndex]
        return nextTrack
    }


}