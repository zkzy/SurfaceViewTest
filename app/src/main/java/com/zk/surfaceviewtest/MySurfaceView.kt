package com.zk.surfaceviewtest

import android.content.Context
import android.content.res.Resources
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer

import android.text.TextUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast

import java.io.File

class MySurfaceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var position = 0
    private var mVideoFile: File? = null
    private var isSurfaceCreated = false

    init {
        initView()
    }

    private fun initView() {
        mSurfaceHolder = holder
        mSurfaceHolder!!.addCallback(this)
        mMediaPlayer = MediaPlayer()
        keepScreenOn = true
        isFocusable = true
        isFocusableInTouchMode = true
        mMediaPlayer!!.setOnCompletionListener { mMediaPlayer!!.start() }
    }

    fun playVideo(path: String, needResize: Boolean) {
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(context, "请输入正确的视频地址！", Toast.LENGTH_SHORT).show()
            return
        }
        playVideo(File(path), needResize)
    }

    fun playVideo(file: File?, needResize: Boolean) {
        if (file == null || !file.exists()) {
            Toast.makeText(context, "请输入正确的视频地址！", Toast.LENGTH_SHORT).show()
            return
        }
        if (needResize) getVideoSize(file.absolutePath)
        mVideoFile = file
        play(position)
    }

    private fun play(position: Int) {
        if (!isSurfaceCreated) return
        if (mVideoFile == null) return
        Log.e(TAG, "play")
        try {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer!!.setDataSource(mVideoFile!!.absolutePath)
            mMediaPlayer!!.setDisplay(holder)
            mMediaPlayer!!.prepareAsync()
            mMediaPlayer!!.setOnPreparedListener {
                mMediaPlayer!!.start()
                mMediaPlayer!!.seekTo(position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated")
        isSurfaceCreated = true
        if (mVideoFile != null && !mMediaPlayer!!.isPlaying) {
            play(position)
            position = 0
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        Log.e(TAG, "surfaceDestroyed")
        isSurfaceCreated = false
        if (mMediaPlayer!!.isPlaying) {
            position = mMediaPlayer!!.currentPosition
            mMediaPlayer!!.stop()
        }
    }

    private fun getVideoSize(path: String) {
        val retr = MediaMetadataRetriever()
        retr.setDataSource(path)
        val _height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT) // 视频高度
        val _width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH) // 视频宽度

        val height = java.lang.Float.valueOf(_height)
        val width = java.lang.Float.valueOf(_width)

        val resources = resources
        val dm = resources.displayMetrics
        val rootWidth = dm.widthPixels
        val rootHeight = dm.heightPixels

        val scale = Math.min(rootWidth / width, rootHeight / height)

        val lp = layoutParams
        lp.height = (height * scale).toInt()
        lp.width = (width * scale).toInt()
        layoutParams = lp
    }

    fun release() {
        if (mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.stop()
        }
        mMediaPlayer!!.release()
        mSurfaceHolder!!.addCallback(null)
    }

    companion object {
        private val TAG = "GameSurfaceView"
    }
}
