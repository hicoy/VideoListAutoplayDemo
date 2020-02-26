package com.example.videorecyclerviewautoplaydemo

import android.content.Context

/*
 * keep application context for using
 */
class ContextProvider {
    companion object {
        private var context: Context? = null

        fun init(context: Context) {
            this.context = context.applicationContext
        }

        fun getApplicationContext(): Context {
            return context?: error("application is null")
        }
    }
}