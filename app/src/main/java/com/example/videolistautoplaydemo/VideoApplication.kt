package com.example.videorecyclerviewautoplaydemo

import android.app.Application

class VideoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextProvider.init(this.applicationContext)
    }
}