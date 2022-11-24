package com.awto.randomjoke

import android.app.Application
import android.util.Log
import com.awto.randomjoke.util.NetworkMonitoringUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    val TAG: String = MyApplication::class.java.simpleName
    var mNetworkMonitoringUtil: NetworkMonitoringUtil? = null

    override fun onCreate() {
        super.onCreate()

        Log.d(
            TAG,
            "onCreate() called"
        )

        mNetworkMonitoringUtil = NetworkMonitoringUtil(applicationContext)
        mNetworkMonitoringUtil!!.checkNetworkState()
        mNetworkMonitoringUtil!!.registerNetworkCallbackEvents()
    }
}