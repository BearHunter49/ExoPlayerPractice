package com.example.exoplayerpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.WindowManager
import android.widget.Toast
import com.pedro.encoder.input.video.CameraOpenException
import com.pedro.rtplibrary.rtmp.RtmpCamera2
import kotlinx.android.synthetic.main.activity_broadcast.*
import net.ossrs.rtmp.ConnectCheckerRtmp

class BroadcastActivity : AppCompatActivity(), ConnectCheckerRtmp, SurfaceHolder.Callback {
    private lateinit var rtmpCamera2: RtmpCamera2
    private val streamUrl = "rtmp://15.164.172.186:1935/bylive/stream"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_broadcast)

        // Instance create
        rtmpCamera2 = RtmpCamera2(surfaceView, this)
        surfaceView.holder.addCallback(this)


        // ---Button----
        btn_stream.setOnClickListener {
            if (!rtmpCamera2.isStreaming) {
                if (rtmpCamera2.prepareAudio() && rtmpCamera2.prepareVideo()) {
                    btn_stream.text = "stop"
                    rtmpCamera2.startStream(streamUrl)
                } else {
                    Toast.makeText(this, "Not prepared", Toast.LENGTH_SHORT).show()
                }
            } else {
                btn_stream.text = "start"
                rtmpCamera2.stopStream()
            }
        }

        btn_switch.setOnClickListener {
            try {
                rtmpCamera2.switchCamera()
            } catch (e: CameraOpenException) {
                e.printStackTrace()
            }
        }
    }



    // Implement Method
    override fun onAuthSuccessRtmp() {
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
    }

    override fun onConnectionSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(this, "Connection Success!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnectionFailedRtmp(reason: String?) {
        runOnUiThread {
            Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAuthErrorRtmp() {
    }

    override fun onDisconnectRtmp() {
        runOnUiThread {
            Toast.makeText(this, "Connection Disconnected!", Toast.LENGTH_SHORT).show()
        }
    }

    // Surface View Holder
    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        rtmpCamera2.startPreview()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        if (rtmpCamera2.isStreaming){
            rtmpCamera2.stopStream()
//            btn_stream.text = "start"
        }
        rtmpCamera2.stopPreview()
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
    }


}
