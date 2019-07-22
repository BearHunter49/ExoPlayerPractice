package com.example.exoplayerpractice

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_playback_control.*
import androidx.core.content.ContextCompat
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.pedro.encoder.input.video.CameraOpenException
import com.pedro.rtplibrary.rtmp.RtmpCamera2
import net.ossrs.rtmp.ConnectCheckerRtmp
import java.util.jar.Manifest


class MainActivity : AppCompatActivity(), ConnectCheckerRtmp, SurfaceHolder.Callback{
    var currentWindow = 0
    var playbackPosition = 0L
    var playWhenReady = true
    private val sample = "https://0ef8937af0317fea.mediapackage.ap-northeast-2.amazonaws.com/out/v1/755374dcef3a4fd2854ef41876b39bf6/index.m3u8"
    lateinit var player: SimpleExoPlayer

    lateinit var mFullScreenDialog: Dialog
    private var mExoPlayerFullscreen = false

    private lateinit var rtmpCamera2: RtmpCamera2
    private val PERMISSIONS = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA)


    // HLS URL 우리꺼
    // https://0ef8937af0317fea.mediapackage.ap-northeast-2.amazonaws.com/out/v1/755374dcef3a4fd2854ef41876b39bf6/index.m3u8
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_main)

        // Permission
        if (!hasPermission(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
        }
        //Fullscreen Dialog 생성해놓기
        initFullscreenDialog()

        //Camera 바인드
        rtmpCamera2 = RtmpCamera2(surfaceView, this)
        surfaceView.holder.addCallback(this)

        // Fullscreen Button ClickEvent
        exo_fullscreen_button.setOnClickListener {
            if (!mExoPlayerFullscreen)
                openFullscreenDialog()
            else
                closeFullscreenDialog()
        }

        // Stream Button ClickEvent
        b_start_stop.setOnClickListener {
            if (!rtmpCamera2.isStreaming){
                if (rtmpCamera2.isRecording || rtmpCamera2.prepareAudio() && rtmpCamera2.prepareVideo()){
                    b_start_stop.text = "Stop Stream"
                    rtmpCamera2.startStream(et_rtp_url.text.toString())
                }
                else {
                    Toast.makeText(this, "오디오, 비디오 준비가 안됨", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                b_start_stop.text = "Stream"
                rtmpCamera2.stopStream()
            }
        }

        // Switch Button ClickEvent
        b_switch_camera.setOnClickListener {
            try {
                rtmpCamera2.switchCamera()
            }catch (e: CameraOpenException){
                e.printStackTrace()
            }
        }

    }

    // Permit 확인
    private fun hasPermission(context: Context, permissions: Array<String>): Boolean {
        for (permit in permissions){
            if (ActivityCompat.checkSelfPermission(context, permit) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }



    //Fullscreen Dialog
    private fun initFullscreenDialog() {
        mFullScreenDialog = object : Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            override fun onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog()
                super.onBackPressed()
            }
        }
    }

    //Fullscreen 열기
    private fun openFullscreenDialog() {

        exoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        (exoPlayerView.parent as ViewGroup).removeView(exoPlayerView)
        mFullScreenDialog.addContentView(
            exoPlayerView,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )

        mExoPlayerFullscreen = true
        mFullScreenDialog.show()
    }

    //Fullscreen 닫기
    private fun closeFullscreenDialog() {
        exoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        (exoPlayerView.parent as ViewGroup).removeView(exoPlayerView)

        // ConstraintLayoutParams (뷰 크기)
        val newConstParam = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        exoPlayerView.layoutParams = newConstParam
        (const_layout as ConstraintLayout).addView(exoPlayerView)

        //ConstraintSet 만들기
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(const_layout)
            connect(exoPlayerView.id, ConstraintSet.BOTTOM, const_layout.id, ConstraintSet.BOTTOM)
            applyTo(const_layout)
        }

        mExoPlayerFullscreen = false
        mFullScreenDialog.dismiss()
    }

    //onStart() 생명주기기 플레이어 생성
    override fun onStart() {
        super.onStart()
        initializePlayer()
    }


    override fun onStop() {
        super.onStop()
        releasePlayer()
    }


    //플레이어 생성
    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this.applicationContext)
        exoPlayerView?.player = player

        // Custom
//        exoPlayerView.useController = false
        exoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        player.seekTo(currentWindow, playbackPosition)

        // EventListner
//        player.addListener(object: Player.EventListener{
//            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//                if (playbackState == Player.STATE_ENDED){
////                    player.seekTo(0)
//                    playbackPosition = 0L
//
//                }
//            }
//
//        })


        val mediaSource = buildMediaSource(Uri.parse(sample))


        // VOD만 루프 되나봐...
        val loopingSource = LoopingMediaSource(mediaSource, 1)

        player.prepare(loopingSource)
        player.playWhenReady = playWhenReady
    }

    //미디어소스(코덱 종류) 빌드
    private fun buildMediaSource(parse: Uri?): MediaSource? {
        val userAgent = Util.getUserAgent(this, "Exo2")
        return HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(parse)
    }

    //플레이어 끄기
    private fun releasePlayer() {
        playbackPosition = player.currentPosition
        currentWindow = player.currentWindowIndex
        playWhenReady = player.playWhenReady

        exoPlayerView.player = null
        player.release()
    }

    //----------------RTMP------------------

    override fun onAuthSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "권한 있음(성공)", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
    }

    override fun onConnectionSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "Connection success", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onConnectionFailedRtmp(reason: String?) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "연결 실패", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAuthErrorRtmp() {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "권한 문제", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDisconnectRtmp() {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "연결 Destroyed", Toast.LENGTH_SHORT).show()
        }
    }

    // ---------SurfaceView---------
    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        rtmpCamera2.startPreview()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        if (rtmpCamera2.isStreaming){
            rtmpCamera2.stopStream()
            b_start_stop.text = "Stream"
        }
        rtmpCamera2.stopPreview()
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
    }

}
