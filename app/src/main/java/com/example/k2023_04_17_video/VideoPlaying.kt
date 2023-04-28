package com.example.k2023_04_17_video

import android.media.AudioAttributes
import android.media.MediaPlayer


class VideoPlaying {
    private  var videoPlay: MediaPlayer =  MediaPlayer()

    fun createradio(link: String) {
        videoPlay = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    fun stopvid() {
        videoPlay.pause()
        videoPlay.reset()
    }

    fun playvid(uri: String) {
        videoPlay.reset()
        videoPlay.setDataSource(uri)
        videoPlay.setOnPreparedListener {
            it.start()
        }
        videoPlay.prepareAsync()
    }

}
