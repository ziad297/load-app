package com.udacity

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


private const val notification_id = 0

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(context: Context, downloadResult: String, file: String) {
    val detailActivityIntent = Intent(
        context,
        DetailActivity::class.java).apply {
            putExtra("download_result_extra", downloadResult)
            putExtra("filename_extra", file)
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        notification_id,
        detailActivityIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationBuilder = NotificationCompat.Builder(
        context,
        context.getString(R.string.channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(R.string.notification_title.toString())
        .setContentText(R.string.notification_description.toString())
        .setContentIntent(pendingIntent)
        .setAutoCancel(false)
        .addAction(
            R.drawable.abc_vector_test,
            context.getString(R.string.notification_button),
            pendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(notification_id, notificationBuilder.build())

}