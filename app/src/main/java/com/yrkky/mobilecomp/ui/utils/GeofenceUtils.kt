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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.yrkky.core.domain.entity.Reminder
import com.yrkky.mobilecomp.Graph
import com.yrkky.mobilecomp.R
import kotlin.random.Random

const val GEOFENCE_RADIUS = 200
const val GEOFENCE_DWELL_DELAY =  2 * 1000
private val TAG: String = "GeofenceUtils"

class GeofenceUtils(private val context: Context) {

    fun createGeofence(reminder: Reminder) {

        Log.i(TAG, "Inside createGeofence")
        val geofencingClient = LocationServices.getGeofencingClient(context)

        val geofence = Geofence.Builder()
            .setRequestId(reminder.reminderId.toString())
            .setCircularRegion(reminder.location_x!!, reminder.location_y!!, GEOFENCE_RADIUS.toFloat())
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(GEOFENCE_DWELL_DELAY)
            .setNotificationResponsiveness(500)
            .build()

        val geofenceRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val intent = Intent(context, GeofenceReceiver::class.java)
            .putExtra("reminder_id", reminder.reminderId)
            .putExtra("title", "Near reminder: ${reminder.title}")
            .putExtra(
                "message",
                "Content: ${reminder.message} Location: ${reminder.location_x}, ${reminder.location_y}"
            )

        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "No permissions to get background location")
                return
            } else {
                geofencingClient.addGeofences(geofenceRequest, pendingIntent)
                Log.i(TAG, "Geofence Added 1")
            }
        } else {
            geofencingClient.addGeofences(geofenceRequest, pendingIntent)
            Log.i(TAG, "Geofence Added 2")
        }
    }

    companion object {
        fun removeGeofences(context: Context, triggeringGeofenceList: MutableList<Geofence>) {
            val geofenceIdList = mutableListOf<String>()
            for (entry in triggeringGeofenceList) {
                geofenceIdList.add(entry.requestId)
            }
            LocationServices.getGeofencingClient(context).removeGeofences(geofenceIdList)
        }
    }

}



fun showNotification(context: Context?, title: String, message: String) {
    Log.i(TAG, "showNotification")
    val intent = Intent()
    intent.setClassName(context!!, "com.yrkky.mobilecomp.MainActivity").apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val CHANNEL_ID = "REMINDER_NOTIFICATION_CHANNEL"
    var notificationId = 1589
    notificationId += Random(notificationId).nextInt(1, 30)

    val notificationBuilder = NotificationCompat.Builder(context!!.applicationContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Nearby reminder: ${title}")
        .setContentText("Message: ${message}")
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText("Message: ${message}")
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.app_name)
        }
        notificationManager.createNotificationChannel(channel)
    }
    notificationManager.notify(notificationId, notificationBuilder.build())
}

private fun requestPermission(
    context: Context,
    permission: String,
    requestPermission: () -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            context,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        requestPermission()
    }
}