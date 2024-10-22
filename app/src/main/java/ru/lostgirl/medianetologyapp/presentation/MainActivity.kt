package ru.lostgirl.medianetologyapp.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.lostgirl.medianetologyapp.R
import ru.lostgirl.medianetologyapp.api.AlbumRepository
import ru.lostgirl.medianetologyapp.databinding.ActivityMainBinding
import ru.lostgirl.medianetologyapp.media.MediaLifecycleObserver
import ru.lostgirl.medianetologyapp.presentation.adapter.OnInteractionListener
import ru.lostgirl.medianetologyapp.presentation.adapter.TrackAdapter
import ru.lostgirl.medianetologyapp.presentation.uimodel.MediaState
import ru.lostgirl.medianetologyapp.presentation.uimodel.TrackUiModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val mediaObserver = MediaLifecycleObserver()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        lifecycle.addObserver(mediaObserver)

        binding.btnAlbomPleer.setOnClickListener {
            val isPaying = mediaObserver.player?.isPlaying
            var activeTrack = viewModel.mainState.value?.album?.activeTrack

            if (isPaying == true && activeTrack != null) {
                mediaObserver.player!!.pause()
                viewModel.setPause(activeTrack)
            }
            if (activeTrack == null) {
                activeTrack = viewModel.mainState.value?.album?.tracks?.first()
                if (activeTrack != null) {
                    playTrack(activeTrack)

                }
            } else if (isPaying == false) {
                mediaObserver.player!!.start()
                viewModel.setActiveTrack(activeTrack)
            }
        }

        mediaObserver.player?.setOnCompletionListener {
            val nextTrack = viewModel.getNext()
            playTrack(nextTrack)
        }

        val adapter = TrackAdapter(object : OnInteractionListener {
            override fun onMediaClick(track: TrackUiModel) {
                val isPaying = mediaObserver.player?.isPlaying
                if (isPaying == true) {
                    mediaObserver.player!!.pause()
                    viewModel.setPause(viewModel.mainState.value?.album?.activeTrack!!)
                }
                if (viewModel.mainState.value?.album?.activeTrack?.id != track.id) {
                    playTrack(track)
                } else if (isPaying == false) {
                    mediaObserver.player!!.start()
                    viewModel.setActiveTrack(track)
                }

            }
        })
        binding.rcAlbom.adapter = adapter

        viewModel.mainState.observe(this) {
            binding.title.text = it.album.title
            binding.artist.text = it.album.artist
            binding.published.text = it.album.published
            binding.style.text = it.album.genre
            binding.btnAlbomPleer.setImageResource(
                if (it.album.mediaState == MediaState.PLAY) R.drawable.ic_pause
                else R.drawable.ic_play
            )
            adapter.submitList(it.album.tracks)
        }

        setContentView(binding.root)
    }

    private fun playTrack(track: TrackUiModel) {
        mediaObserver.player!!.reset()
        mediaObserver.apply {
            player?.setDataSource(
                AlbumRepository.BASE_MEDIA_URL + track.file
            )
        }.play()
        viewModel.setActiveTrack(track)
    }
}