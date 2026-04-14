package com.uzair.pixel.test.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

interface NetworkConnectivityMonitor {
    fun isAvailable(): Boolean
    val isConnected: Flow<Boolean>
}

class NetworkConnectivityMonitorImpl(
    private val connectivityManager: ConnectivityManager
) : NetworkConnectivityMonitor {
    override fun isAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    override val isConnected: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(isAvailable())
            }

            override fun onLost(network: Network) {
                trySend(isAvailable())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(isAvailable())
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        trySend(isAvailable())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}
