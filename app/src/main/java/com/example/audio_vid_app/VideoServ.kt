package com.example.audio_vid_app

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder

class VideoServ : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder = vidbind()

    inner class vidbind : Binder() {
        fun getInstance(): VideoServ = this@VideoServ
    }

    fun toggleRadio(radioUrl: String) {
        mediaPlayer?.release()  // Release previous media player
        mediaPlayer = MediaPlayer().apply {
            setDataSource(this@VideoServ, Uri.parse(radioUrl))
            prepare()
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
