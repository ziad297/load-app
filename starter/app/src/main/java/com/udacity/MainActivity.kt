package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var file = ""
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
        when(radio.checkedRadioButtonId){
            R.id.glide -> {
                download("https://github.com/bumptech/glide")
                file = getString(R.string.glide)
                custom_button.buttonState(ButtonState.Loading)
            }
            R.id.loadApp -> {
                download("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter")
                file = getString(R.string.load_app)
                custom_button.buttonState(ButtonState.Loading)
            }
            R.id.retrofit -> {
                download("https://github.com/square/retrofit")
                file = getString(R.string.retrofit)
                custom_button.buttonState(ButtonState.Loading)
            }

            else -> Toast.makeText(this, "please select link to download", Toast.LENGTH_SHORT)
                .show()
        }
            buildNotificationChannel(getString(R.string.channel_id))
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildNotificationChannel(channelId: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            "downloadChannel",
            NotificationManager.IMPORTANCE_HIGH
        )

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val notificationManager = ContextCompat.getSystemService(
                context!!,
                NotificationManager::class.java
            ) as NotificationManager

            var downloadResult = ""
            val query = DownloadManager.Query().setFilterById(id!!)
            val cursor = downloadManager.query(query)

            if (cursor.moveToNext()){
                val coloumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val downloadState = cursor.getInt(7)

                when(downloadState){
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        downloadResult = "Successful"
                        Toast.makeText(this@MainActivity, downloadResult, Toast.LENGTH_SHORT).show()
                    }
                    DownloadManager.STATUS_FAILED -> {
                        downloadResult = "Failed"
                        Toast.makeText(this@MainActivity, downloadResult, Toast.LENGTH_SHORT).show()
                    }
                }
                notificationManager.sendNotification(context, downloadResult, file)
                custom_button.buttonState(ButtonState.Completed)
            }

        }
    }

    private fun download(url:String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}


