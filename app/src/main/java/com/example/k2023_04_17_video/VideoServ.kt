package com.example.k2023_04_17_video

import android.content.Intent
import android.os.*
import android.app.Service

var val_: Int = 0
var stats: Boolean = false

class VideoServ: Service() {

    private lateinit var Loopr: Looper
    private lateinit var handler_s: ServiceHandler

    private val audio: VideoPlaying = VideoPlaying()
    private var whatMedia: Int = 0

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        super.onCreate()

        HandlerThread("Handler", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            Loopr = looper
            handler_s = ServiceHandler(Loopr)
        }
    }


    override fun onStartCommand(a: Intent?, b: Int, c: Int): Int {
        super.onStartCommand(a, b, c)

        handler_s.obtainMessage().also { msg ->
            msg.arg1 = c
            handler_s.sendMessage(msg)

        }
        return START_STICKY
    }


    private var binder = vidbind()

    override fun onBind(sevice: Intent?): IBinder? {
        return binder
    }



    inner class vidbind : Binder() {
        fun getInstance() : VideoServ = this@VideoServ
    }
}
