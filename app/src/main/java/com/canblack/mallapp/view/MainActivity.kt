package com.canblack.mallapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canblack.mallapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        val uri = Uri.parse(
            "android.resource://"
                    + packageName
                    + "/"
                    + R.raw.background
        )

        videoView.setVideoURI(uri)
        videoView.start()
        videoView.setOnPreparedListener {
            it.isLooping = true
            it.setVolume(0f,0f)
            it.start()
            }

        btn_giris.setOnClickListener {
            val int = Intent(this, MallAct::class.java)
            startActivity(int)
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42)
            }
        }
    }
}
