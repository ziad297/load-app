package com.udacity

import android.annotation.SuppressLint
import android.graphics.Color.green
import android.graphics.Color.red
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val intent = this.intent
        val state = intent.getStringExtra("download_state")
        val fileName = intent.getStringExtra("filename_extra")

        fileNameTV.text = fileName
        if (state == "Successful"){
        dowloadState.text = "Successful"
        dowloadState.setTextColor(getColor(R.color.green))
        }else if(state == "Failed"){
            dowloadState.text = "Failed"
            dowloadState.setTextColor(getColor(R.color.red))
        }


    }

}
