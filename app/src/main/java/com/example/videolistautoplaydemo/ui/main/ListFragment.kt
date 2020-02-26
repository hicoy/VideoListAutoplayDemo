package com.example.videorecyclerviewautoplaydemo.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videorecyclerviewautoplaydemo.ContextProvider
import com.example.videorecyclerviewautoplaydemo.R
import com.google.android.exoplayer2.SimpleExoPlayer

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    lateinit var adapter: VideoListAdapter
    lateinit var videoPlayManager: VideoPlayManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        val player = SimpleExoPlayer.Builder(requireContext()).build()
        adapter = VideoListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearLayoutManager
        videoPlayManager =
            VideoPlayManager(requireContext(), player, recyclerView, lifecycle)

        adapter.submitList(getVideoList())
    }

    override fun onResume() {
        super.onResume()
        recyclerView.post { videoPlayManager.playAt(0) }
    }

    private fun getVideoList(): MutableList<VideoInfo> {
        val list = mutableListOf<VideoInfo>()
        repeat(10) {
            val info =
                VideoInfo(
                    Uri.parse(R.string.video_url.resourceString())
                )
            list.add(info)
        }
        return list
    }
}

fun Int.resourceString(): String {
    return ContextProvider.getApplicationContext().getString(this)
}
