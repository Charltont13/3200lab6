package com.example.k2023_04_17_video


import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaDrm
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.WindowManager
import android.widget.*
import com.example.k2023_04_17_video.VideoPlaying.*
import com.example.k2023_04_17_video.VideoServ.*


lateinit var videoServ: VideoServ
private var bnd: Boolean = false
private var message: Message = Message()


class MainActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener, MediaPlayer.OnDrmInfoListener,
    SurfaceHolder.Callback {

    private val videolink = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    private lateinit var videoView: VideoView
    private lateinit var Video_clicker: Button
    private lateinit var mediaController: MediaController
    private lateinit var mediaPlayer: MediaPlayer
    private var status_vid: Boolean = false
    private var On_init: Boolean = true
    private lateinit var RadioListener: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //RadioListener = findViewById(R.id.radio1)
        //RadioListener.setOnClickListener {
        //    videoServ.RadioListener("http://streaming.radionomy.com/JamendoLounge") }

        // RadioListener = findViewById(r.id.radio)
        // RadioListener.setOnClickListener {
        //    videoServ.RadioListener("https://media-ssl.musicradio.com/ClassicFMMP3") }

        videoView = findViewById(R.id.videoView)
        Video_clicker = findViewById(R.id.video_toggle_button)

        mediaController = MediaController(this)
        videoView.holder.addCallback(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        videoView.holder.addCallback(this)

        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnPreparedListener(this)

        Video_clicker.setOnClickListener {
            if (status_vid) {
                mediaPlayer.pause()
                mediaPlayer.stop()
            } else {
                if (On_init) {
                    mediaPlayer.prepareAsync()
                    On_init = false
                } else {
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(applicationContext, Uri.parse(videolink))
                    mediaPlayer.prepareAsync()
                }
            }
            status_vid = if (status_vid) false else true
        }

    }

    override fun onStart() {
        super.onStart()

        Intent(this, VideoServ::class.java).run {
            bindService(this, serviceconnec, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()

        unbindService(serviceconnec)
        bnd = false
    }

    private val serviceconnec: ServiceConnection = object: ServiceConnection{
        override fun onServiceConnected(namea: ComponentName?, service: IBinder?) {
            val binding: VideoServ.vidbind = service as VideoServ.vidbind
            videoServ = binding.getInstance()
            bnd = true
        }

        override fun onServiceDisconnected(namea: ComponentName?) {
            bnd = false
        }

    }


    override fun onDrmInfo(mediaPlayer: MediaPlayer, inf: MediaPlayer.DrmInfo?) {
        inf?.supportedSchemes?.get(0)?.let { drmScheme ->
            mediaPlayer.prepareDrm(drmScheme)
            val keyRequest = mediaPlayer.getKeyRequest(
                null, null, null,
                MediaDrm.KEY_TYPE_STREAMING, null
            )
            mediaPlayer.provideKeyResponse(null, keyRequest.data)
        }
    }



    override fun onPrepared(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.start()
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        mediaPlayer.apply {
            setOnDrmInfoListener(this@MainActivity)
            setDataSource(applicationContext, Uri.parse(videolink))
            setDisplay(surfaceHolder)
        }
    }

    override fun surfaceChanged(namea: SurfaceHolder, nameb: Int, namec: Int, named: Int) {
        mediaPlayer.apply {
            setDisplay(namea)
        }
    }

    override fun surfaceDestroyed(namea: SurfaceHolder) {
    }

}

