package com.example.exoplayerpractice

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    private val PERMISSIONS = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA)

    // HLS URL 우리꺼
    // https://0ef8937af0317fea.mediapackage.ap-northeast-2.amazonaws.com/out/v1/755374dcef3a4fd2854ef41876b39bf6/index.m3u8
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Permission
        if (!hasPermission(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
        }



        btn_broadcast.setOnClickListener {
            startActivity(Intent(this, BroadcastActivity::class.java))
        }

        btn_live.setOnClickListener {
            startActivity(Intent(this, LiveActivity::class.java))
        }
    }

    // Permit 확인
    private fun hasPermission(context: Context, permissions: Array<String>): Boolean {
        for (permit in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permit) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }
}
