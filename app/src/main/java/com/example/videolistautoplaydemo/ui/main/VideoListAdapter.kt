package com.example.videorecyclerviewautoplaydemo.ui.main

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.videorecyclerviewautoplaydemo.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class VideoListAdapter : ListAdapter<VideoInfo, RecyclerView.ViewHolder>(itemCallback) {

    companion object {
        private val itemCallback = object: ItemCallback<VideoInfo>() {
            override fun areItemsTheSame(oldItem: VideoInfo, newItem: VideoInfo): Boolean {
                return oldItem.uri == newItem.uri
            }

            override fun areContentsTheSame(oldItem: VideoInfo, newItem: VideoInfo): Boolean {
                return oldItem.uri == newItem.uri
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoViewHolder) {
            holder.pauseVideo()
            holder.bind(currentList[position])
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        when (holder) {
            is VideoViewHolder -> {
                holder.pauseVideo()
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            return super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                when(it) {
                    is VideoAction.PLAY -> {
                        val action = payloads[0] as VideoAction.PLAY
                        val videoViewHolder = holder as VideoViewHolder
                        videoViewHolder.playVideo(action.player)
                    }
                    is VideoAction.PAUSE -> {
                        val videoViewHolder = holder as VideoViewHolder
                        videoViewHolder.pauseVideo()
                    }
                }
            }
        }
    }
}

data class VideoInfo(val uri: Uri, var position: Long = 0)

class VideoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val TAG = "VideoViewHolder"
    }
    var videoInfo: VideoInfo? = null
    val playerView: PlayerView = itemView.findViewById(R.id.playerView)

    var videoDuration: Long = 0

    private val eventListener = object: Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                val player = playerView.player
                if (player != null) {
                    videoDuration = playerView.player?.duration?: 0
                    if (videoInfo?.position?: 0 < player.duration) {
                        player.seekTo(videoInfo?.position ?: 0)
                        Log.e(TAG,"restore seekTo: ${videoInfo?.position}")
                    }
                    playerView.onResume()
                    playerView.player?.removeListener(this)
                }
            }
        }
    }

    fun bind(info: VideoInfo) {
        this.videoInfo = info
    }

    fun pauseVideo() {
        if (playerView.player?.isPlaying == true) {
            videoInfo?.apply {
                position = playerView.player?.currentPosition ?: 0
                Log.e(TAG,"save video time: ${playerView.player?.currentPosition}")
            }
            playerView.onPause()
            playerView.player = null
            playerView.player?.stop()
        }
    }

    fun playVideo(player: SimpleExoPlayer) {
        videoInfo?.apply {
            playerView.player = player
            player.playWhenReady = true
            val dataSourceFactory = DefaultDataSourceFactory(
                itemView.context,
                Util.getUserAgent(itemView.context, "VideoDemo")
            )
            val videoSource =
                ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            player.prepare(videoSource)
            player.addListener(eventListener)
        }
    }
}

sealed class VideoAction {
    class PLAY(val player: SimpleExoPlayer): VideoAction()
    object PAUSE : VideoAction()
}