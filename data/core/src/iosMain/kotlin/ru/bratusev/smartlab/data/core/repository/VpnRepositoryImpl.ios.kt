package ru.bratusev.smartlab.data.core.repository

import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import platform.darwin.getifaddrs
import platform.darwin.ifaddrs
import ru.bratusev.smartlab.domain.core.repository.VpnRepository

actual class VpnRepositoryImpl : VpnRepository {
    @OptIn(ExperimentalForeignApi::class)
    actual override fun isVpnActive(): Boolean {
        var isVpn = false

        memScoped {
            // Allocate a pointer for the linked list of interfaces
            val ifap = alloc<CPointerVar<ifaddrs>>()

            // getifaddrs returns 0 on success
            if (getifaddrs(ifap.ptr) == 0) {
                var current = ifap.value

                // Iterate through the linked list
                while (current != null) {
                    val interfaceData = current.pointed
                    val name = interfaceData.ifa_name?.toKString() ?: ""

                    // "utun" is the most common prefix for VPN tunnels on iOS
                    // "ppp" (Point-to-Point) and "ipsec" are also possibilities
                    if (isTunnelInterface(name)) {
                        isVpn = true
                        break
                    }

                    current = interfaceData.ifa_next
                }

                // Free the memory allocated by the OS
                platform.darwin.freeifaddrs(ifap.value)
            }
        }

        return isVpn
    }

    private fun isTunnelInterface(name: String): Boolean {
        // Most modern VPNs on iOS use "utun" (e.g., utun0, utun1)
        // WireGuard, OpenVPN, and IPsec usually create these.
        return name.contains("utun") ||
                name.contains("ppp") ||
                name.contains("ipsec") ||
                name.contains("tap") ||
                name.contains("tun")
    }
}