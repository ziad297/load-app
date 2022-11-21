package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downlodID: Long = 0
    private var name = ""

    companion object Constants {

        private const val udacityStarterURL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"

        private const val glideURL = "https://github.com/bumptech/glide"

        private const val retrofitURL = "https://github.com/square/retrofit"

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            when (radio.checkedRadioButtonId) {
                R.id.glide -> {
                    custom_button.buttonState(ButtonState.Loading)
                    name = getString(R.string.glide)
                    download(glideURL)
                }

                R.id.loadApp -> {
                    custom_button.buttonState(ButtonState.Loading)
                    name = getString(R.string.load_app)
                    download(udacityStarterURL)
                }

                R.id.retrofit -> {
                    custom_button.buttonState(ButtonState.Loading)
                    name = getString(R.string.retrofit)
                    download(retrofitURL)
                }
                else -> Toast.makeText(this, "please select link to download", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        createChannel(getString(R.string.notification_channel_id))
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val ID = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val tag = "MainActivity"
            Log.d(tag, "onReceive: ${ID.toString()}")
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val notificationManager = ContextCompat.getSystemService(
                context!!,
                NotificationManager::class.java
            ) as NotificationManager

            var downloadStateExtra = ""
            val query = DownloadManager.Query().setFilterById(ID!!)
            val cursor = downloadManager.query(query)

            if (cursor.moveToNext()) {
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                Log.d(tag, "onReceive: $columnIndex")
                val downloadState = cursor.getInt(7)
                Log.d(tag, "onReceive: $downloadState")
                when (downloadState) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        downloadStateExtra = "successful"
                        Toast.makeText(this@MainActivity, downloadStateExtra, Toast.LENGTH_SHORT)
                            .show()
                    }

                    DownloadManager.STATUS_FAILED -> {
                        downloadStateExtra = "Failure"
                        Toast.makeText(this@MainActivity, downloadStateExtra, Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                        Toast.makeText(this@MainActivity, "Download Complete", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }



            notificationManager.sendNotification(context, downloadStateExtra, name)

            custom_button.buttonState(ButtonState.Completed)

        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downlodID =
            downloadManager.enqueue(request) // enqueue puts the download request in the queue.


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(channelId: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            "downloadChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
            .apply {
                setShowBadge(false)
                enableVibration(true)
            }

        val notificationManager = this.getSystemService(NotificationManager::class.java).apply {
            createNotificationChannel(notificationChannel)
        }
    }

}


