package com.udacity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        fileNameTV.text = fileName ?: "N/A"
        if (state == "successful") {
            statusTV.apply {
                text = getString(R.string.succeeded)
                setTextColor(getColor(R.color.green))
            }
        } else if (state == "Failure") {
            statusTV.apply {
                text = getString(R.string.failure)
                setTextColor(getColor(R.color.red))
            }
        }

        okButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }

}
