package com.zk.surfaceviewtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * if you want to run the application
 * first please copy the assets files to sdcard
 * sencond you should config the kotlin
 */


class MainActivity : AppCompatActivity() {
    private var gameSurfaceView: MySurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameSurfaceView = findViewById(R.id.game_surface)
        gameSurfaceView!!.playVideo("/storage/emulated/0/001.mp4", true)
    }

    companion object {
        private val TAG = "MainActivity"
    }
}
