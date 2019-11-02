package com.canblack.mallapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.canblack.mallapp.R
import kotlinx.android.synthetic.main.activity_web.*

class Web : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val intent = intent
        val id = intent.getIntExtra("id",-1)

        if (id == 1){
            webview.loadUrl("https://www.carrefoursa.com/tr/")
            txt_header.text = "Website"
        }
        else if(id == 2){
            val web = intent.getStringExtra("web")
            webview.loadUrl(web)
            txt_header.text = "KatPlanı"
        }

        else if(id == 3){
            webview.loadUrl("https://www.carrefoursa.com/tr/")
            txt_header.text = "Kampanyalar"
        }
    }
}
