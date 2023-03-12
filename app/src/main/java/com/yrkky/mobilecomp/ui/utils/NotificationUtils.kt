package com.yrkky.mobilecomp.ui.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.yrkky.mobilecomp.Graph
import com.yrkky.mobilecomp.R

private const val NOTIFICATION_ID = 33
private const val CHANNEL_ID = "ReminderGeofenceChannel"
private val TAG: String = "NotificationUtils"

fun createChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "Channel1", NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

fun notifyGeofence(title: String, message: String) {
    Log.i(TAG, "showNotification")
    val intent = Intent()
    val context = Graph.appContext
    intent.setClassName(context, "com.yrkky.mobilecomp.MainActivity").apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val notificationId = 10
    val builder = NotificationCompat.Builder(
        Graph.appContext,
        CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Nearby reminder: ${title}")
        .setContentText("Message: ${message}")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)

    with(NotificationManagerCompat.from(Graph.appContext)) {
        if (ActivityCompat.checkSelfPermission(
                Graph.appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(notificationId, builder.build())
    }
}