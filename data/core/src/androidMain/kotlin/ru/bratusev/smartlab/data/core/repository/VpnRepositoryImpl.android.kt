package ru.bratusev.smartlab.data.core.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.bratusev.smartlab.domain.core.repository.VpnRepository
import java.net.NetworkInterface
import java.util.Collections

actual class VpnRepositoryImpl(private val context: Context) : VpnRepository {
    actual override fun isVpnActive(): Boolean {
        // Method 1: Check the standard Android API (TRANSPORT_VPN)
        if (checkTransportVpn()) return true

        // Method 2: Check Network Interface Names (The "iOS Style" fallback)
        // This catches split tunnels and weird OEM implementation bugs
        if (checkNetworkInterfaces()) return true

        return false
    }

    private fun checkTransportVpn(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // 1. Check the currently ACTIVE network (Standard way)
        val activeNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(activeNetwork)
        if (caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true) {
            return true
        }

        // 2. Iterate through ALL networks (Robust way)
        // Sometimes the VPN is connected but not "active" (e.g., split tunnel)
        try {
            val networks = connectivityManager.allNetworks
            for (network in networks) {
                val networkCaps = connectivityManager.getNetworkCapabilities(network)
                if (networkCaps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true) {
                    return true
                }
            }
        } catch (e: Exception) {
            // allNetworks can sometimes throw exceptions on older APIs or restricted states
            e.printStackTrace()
        }

        return false
    }

    private fun checkNetworkInterfaces(): Boolean {
        try {
            val networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces())

            for (networkInterface in networkInterfaces) {
                if (!networkInterface.isUp) continue

                val name = networkInterface.name.lowercase()

                // Common VPN interface names on Android
                if (name.contains("tun") ||   // Standard usage (tun0, tun1)
                    name.contains("ppp") ||   // Point-to-Point (older VPNs)
                    name.contains("pptp") ||  // PPTP
                    name.contains("tap")      // Tap-based VPNs
                ) {
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}