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

const val GEOFENCE_RADIUS = 500
const val GEOFENCE_DWELL_DELAY =  2 * 1000
private val TAG: String = "GeofenceUtils"

class GeofenceUtils(private val context: Context) {

    fun createGeofence(reminder: Reminder) {

        Log.i(TAG, "Inside createGeofence")
        val geofencingClient = LocationServices.getGeofencingClient(context)

        val geofence = Geofence.Builder()
            .setRequestId(reminder.reminderId.toString())
            .setCircularRegion(reminder.location_x, reminder.location_y, GEOFENCE_RADIUS.toFloat())
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
            .putExtra("reminder_id", reminder.reminderId.toString())
            .putExtra("title", "Near reminder: ${reminder.title}")
            .putExtra(
                "message",
                "Content: ${reminder.message} Location: ${reminder.location_x}, ${reminder.location_y}"
            )

        intent.action = "com.yrkky.mobilecomp.ACTION_GEOFENCE_EVENT"

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

    fun removeGeofences(context: Context, triggeringGeofenceList: MutableList<Geofence>) {
        val geofenceIdList = mutableListOf<String>()
        for (entry in triggeringGeofenceList) {
            geofenceIdList.add(entry.requestId)
        }
        LocationServices.getGeofencingClient(context).removeGeofences(geofenceIdList)
    }



}
