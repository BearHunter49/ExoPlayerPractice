package com.example.barcodepractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var integrator: IntentIntegrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        integrator = IntentIntegrator(this)
        integrator.apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            setPrompt("Prompt!")
//            setBarcodeImageEnabled(true)
//            setBeepEnabled(false)
            setOrientationLocked(false)
            captureActivity = CustomScannerActivity::class.java
        }

        // Barcode QR_CODE Generate (EAN_13 안됨)
        btn_gen.setOnClickListener {
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap("1234567890123", BarcodeFormat.QR_CODE, 400, 400)
                img_barcode.setImageBitmap(bitmap)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        // Transition Barcode Capture screen
        btn_scan.setOnClickListener {
            integrator.initiateScan()
//            startActivity(Intent(this, ScanActivity::class.java))
        }


    }

    // Return Result Data
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null){
            if (result.contents == null){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_SHORT).show()
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
