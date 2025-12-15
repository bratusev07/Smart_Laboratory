package ru.bratusev.smartlab.data.core.repository

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import ru.bratusev.smartlab.data.core.remote_storage.Constants
import ru.bratusev.smartlab.domain.core.model.NetworkStatus
import ru.bratusev.smartlab.domain.core.repository.NetworkRepository
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.Collections

actual class NetworkRepositoryImpl(private val context: Context) : NetworkRepository {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    actual override fun getNetworkStatus(): NetworkStatus {
        return NetworkStatus(
            ip = getIpAddress() ?: "0.0.0.0",
            baseUrl = Constants.BASE_URL,
            isVpnActive = isVpnActive()
        )
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun getIpAddress(): String? {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return null
        val linkProperties: LinkProperties = connectivityManager.getLinkProperties(activeNetwork) ?: return null

        return linkProperties.linkAddresses
            .map { it.address }
            .filterIsInstance<Inet4Address>()
            .firstOrNull { !it.isLoopbackAddress }
            ?.hostAddress
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isVpnActive(): Boolean {
        // 1. Check Transport (Standard)
        if (checkTransportVpn()) return true
        // 2. Check Interface Names (Fallback/Samsung/Split-tunnel)
        if (checkNetworkInterfaces()) return true
        return false
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun checkTransportVpn(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Check active network
        val activeCaps = cm.getNetworkCapabilities(cm.activeNetwork)
        if (activeCaps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true) return true

        // Check all networks (background/split-tunnel)
        try {
            cm.allNetworks.forEach { network ->
                val caps = cm.getNetworkCapabilities(network)
                if (caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true) return true
            }
        } catch (e: Exception) {
            // allNetworks can fail on some Android versions/states
        }
        return false
    }

    private fun checkNetworkInterfaces(): Boolean {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                if (!intf.isUp) continue
                val name = intf.name.lowercase()
                if (name.contains("tun") || name.contains("ppp") || name.contains("pptp")) {
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}