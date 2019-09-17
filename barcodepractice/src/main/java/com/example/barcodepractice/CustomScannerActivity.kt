package com.example.barcodepractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.CaptureManager
import kotlinx.android.synthetic.main.activity_custom_scanner.*

class CustomScannerActivity : AppCompatActivity() {

    lateinit var manager: CaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_scanner)

        manager = CaptureManager(this, decor_barcodeView)
        manager.initializeFromIntent(intent, savedInstanceState)
        manager.decode()
    }

    override fun onResume() {
        super.onResume()
        manager.onResume()
    }

    override fun onPause() {
        super.onPause()
        manager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.onDestroy()
    }

}
