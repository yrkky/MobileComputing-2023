package com.yrkky.mobilecomp.ui.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.LocationServices
import java.lang.Thread.sleep

private val TAG: String = "GeofenceReceiver"

class GeofenceReceiver : BroadcastReceiver(){

    lateinit var reminder_id: String
    lateinit var title: String
    lateinit var message: String

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.i(TAG, "onReceive Geofence Event")

        if (context != null && intent != null) {
            Log.i(TAG, "onReceive: context & intent not null")
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            val geofencingTransition = geofencingEvent.geofenceTransition

            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
                Log.e("GeofenceReceiver", errorMessage)
                return
            }

            if (intent != null) {
                reminder_id = intent.getStringExtra("reminder_id")!!
                title = intent.getStringExtra("title")!!
                message = intent.getStringExtra("message")!!
                Log.i(TAG, "onReceive intent content: ${reminder_id} ${title} ${message}")
            }

            if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofencingTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                val triggeringGeofences = geofencingEvent.triggeringGeofences
                Log.i(TAG, "onReceive transition enter")

                val notificationHelper = NotificationHelper(context)
                notificationHelper.notifyGeofence(title, message)

                if (triggeringGeofences.isNullOrEmpty()) {
                    return
                }
                removeGeofences(context, triggeringGeofences)

            }
        }


    }

    private fun removeGeofences(context: Context, triggeringGeofenceList: MutableList<Geofence>) {
        val geofenceIdList = mutableListOf<String>()
        for (entry in triggeringGeofenceList) {
            geofenceIdList.add(entry.requestId)
        }
        LocationServices.getGeofencingClient(context).removeGeofences(geofenceIdList)
    }


}