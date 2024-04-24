package com.example.audio_vid_app

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.k2023_04_17_video.R

class MainActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var playVideoButton: Button
    private lateinit var toggleVideoButton: Button
    private lateinit var playAudioButton: Button
    private lateinit var toggleAudioButton: Button
    private var audioPlayer: MediaPlayer? = null
    private var currentVideoIndex = 0
    private var currentAudioIndex = 0

    private val videoResources = arrayOf(R.raw.muhammadali, R.raw.canelo)
    private val audioResources = arrayOf(R.raw.hammer, R.raw.lebron)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupListeners()
        prepareVideoResource(currentVideoIndex)
        prepareAudioResource(currentAudioIndex)
    }

    private fun initViews() {
        videoView = findViewById(R.id.videoView)
        playVideoButton = findViewById(R.id.play_video_button)
        toggleVideoButton = findViewById(R.id.toggle_video_button)
        playAudioButton = findViewById(R.id.play_audio_button)
        toggleAudioButton = findViewById(R.id.toggle_audio_button)
    }

    private fun setupListeners() {
        playVideoButton.setOnClickListener {
            if (!videoView.isPlaying) {
                videoView.start()
                playVideoButton.text = getString(R.string.pause_video)
            } else {
                videoView.pause()
                playVideoButton.text = getString(R.string.play_video)
            }
        }

        toggleVideoButton.setOnClickListener {
            currentVideoIndex = (currentVideoIndex + 1) % videoResources.size
            prepareVideoResource(currentVideoIndex)
        }

        playAudioButton.setOnClickListener {
            audioPlayer?.let { player ->
                if (!player.isPlaying) {
                    player.start()
                    playAudioButton.text = getString(R.string.pause_audio)
                } else {
                    player.pause()
                    playAudioButton.text = getString(R.string.play_audio)
                }
            } ?: run {
                prepareAudioResource(currentAudioIndex)
                audioPlayer?.start()
            }
        }

        toggleAudioButton.setOnClickListener {
            currentAudioIndex = (currentAudioIndex + 1) % audioResources.size
            prepareAudioResource(currentAudioIndex)
        }
    }

    private fun prepareVideoResource(index: Int) {
        val videoUri = Uri.parse("android.resource://${packageName}/${videoResources[index]}")
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.setVolume(0f, 0f)
            playVideoButton.text = getString(R.string.play_video)
        }
        videoView.pause()
    }

    private fun prepareAudioResource(index: Int) {
        audioPlayer?.release()
        val audioUri = Uri.parse("android.resource://${packageName}/${audioResources[index]}")
        audioPlayer = MediaPlayer.create(this, audioUri).also { mediaPlayer ->
            mediaPlayer.isLooping = true
            playAudioButton.text = getString(R.string.play_audio)
        }
        audioPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
        audioPlayer?.release()
    }
}
