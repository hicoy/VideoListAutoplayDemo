package com.example.videorecyclerviewautoplaydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.videorecyclerviewautoplaydemo.R
import com.example.videorecyclerviewautoplaydemo.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitAllowingStateLoss()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}
