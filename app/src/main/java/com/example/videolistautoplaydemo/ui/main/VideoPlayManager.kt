package com.example.videorecyclerviewautoplaydemo.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class VideoPlayManager(
    context: Context,
    private val player: SimpleExoPlayer,
    recyclerView: RecyclerView,
    lifeCycle: Lifecycle) {

    companion object {
        private val TAG = "VideoPlayManager"
    }

    private val dataSourceFactory: DataSource.Factory
    private var playingPos = RecyclerView.NO_POSITION
    private val adapter: VideoListAdapter
    private val linearLayoutManager: LinearLayoutManager

    init {
        dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "VideoDemo")
        )
        adapter = recyclerView.adapter as VideoListAdapter
        linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // play First Completely Visible Item
                    val playPos = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    Log.e(TAG,"play position $playPos")
                    playAt(playPos)
                }
            }
        })
        lifeCycle.addObserver(object: LifecycleObserver{
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy(owner: LifecycleOwner) {
                Log.e(TAG,"release VideoPlayManager")
                release()
            }
        })
    }

    fun playAt(index: Int) {
        if (index == playingPos || index == RecyclerView.NO_POSITION) return
        playingPos = index
        adapter.notifyItemChanged(index, VideoAction.PLAY(player))
    }

    fun release() {
        player.release()
    }
}