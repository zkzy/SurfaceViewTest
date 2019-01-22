package com.zk.surfaceviewtest

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View


class SplashActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val surfaceView = findViewById<MySurfaceView>(R.id.splash_surface)
        surfaceView.playVideo("/storage/emulated/0/start.mp4", false)
        findViewById<View>(R.id.btn_start).setOnClickListener {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            surfaceView.release()
            finish()
        }
    }


}