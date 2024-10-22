package ru.lostgirl.medianetologyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.lostgirl.medianetologyapp.R
import ru.lostgirl.medianetologyapp.databinding.TrackItemBinding
import ru.lostgirl.medianetologyapp.presentation.uimodel.MediaState
import ru.lostgirl.medianetologyapp.presentation.uimodel.TrackUiModel

interface OnInteractionListener {
    fun onMediaClick(track: TrackUiModel)
}

class TrackAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<TrackUiModel, TrackViewHolder>(TrackDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }
}

class TrackViewHolder(
    private val binding: TrackItemBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: TrackUiModel) {
        binding.apply {
            trackName.text = track.file
            albumName.text = track.albumName
            binding.btnTrackPleer.setImageResource(
                if (track.mediaState == MediaState.PLAY)
                    R.drawable.ic_pause
                else R.drawable.ic_play
            )
            btnTrackPleer.setOnClickListener {
                onInteractionListener.onMediaClick(track)
            }
        }
    }
}

class TrackDiffCallback : DiffUtil.ItemCallback<TrackUiModel>() {
    override fun areItemsTheSame(oldItem: TrackUiModel, newItem: TrackUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TrackUiModel, newItem: TrackUiModel): Boolean {
        return oldItem == newItem
    }
}