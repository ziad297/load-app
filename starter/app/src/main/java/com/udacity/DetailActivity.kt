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
        val tag = "DetailActivity"
        val intent = this.intent
        val s = intent.getStringExtra("download_state")
        val name = intent.getStringExtra("filename_extra")
        Log.d(tag, "onCreate: $s")
        Log.d(tag, "onCreate: $name")
        fileNameTV.text = name ?: "N/A"

            statusTV.apply {
                text = getString(R.string.succeeded)
                setTextColor(getColor(R.color.green))
            }
        if (s == "Failure") {
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
