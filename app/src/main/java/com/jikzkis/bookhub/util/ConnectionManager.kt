package com.jikzkis.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {

    fun checkConnectivity(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activityNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        if(activityNetwork?.isConnected != null) {
            return activityNetwork.isConnected
        } else {
            return false
        }
    }

}