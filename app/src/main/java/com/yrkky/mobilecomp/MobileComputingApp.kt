package com.yrkky.mobilecomp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MobileComputingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }

}