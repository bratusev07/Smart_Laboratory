package ru.bratusev.smartlab.data.core.repository

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import platform.darwin.freeifaddrs
import platform.darwin.getifaddrs
import platform.darwin.ifaddrs
import platform.darwin.inet_ntop
import platform.posix.AF_INET
import platform.posix.INET_ADDRSTRLEN
import platform.posix.sockaddr
import platform.posix.sockaddr_in
import ru.bratusev.smartlab.domain.core.model.NetworkStatus
import ru.bratusev.smartlab.domain.core.repository.NetworkRepository

actual class NetworkRepositoryImpl(
    val serverSelectionRepositoryImpl: ServerSelectionRepositoryImpl
) : NetworkRepository {
    @OptIn(ExperimentalForeignApi::class)
    actual override fun getNetworkStatus(): NetworkStatus {
        var foundIp: String? = null
        var isVpn = false

        memScoped {
            // Explicit type definition to avoid compiler ambiguity
            val ifap: CPointerVar<ifaddrs> = alloc()

            if (getifaddrs(ifap.ptr) == 0) {
                var current = ifap.value

                while (current != null) {
                    val interfaceData = current.pointed
                    val name = interfaceData.ifa_name?.toKString() ?: ""
                    val sa = interfaceData.ifa_addr

                    // 1. Detect VPN based on interface name
                    if (!isVpn) {
                        if (name.contains("utun") ||
                            name.contains("ppp") ||
                            name.contains("ipsec") ||
                            name.contains("tap") ||
                            name.contains("tun")
                        ) {
                            isVpn = true
                        }
                    }

                    // 2. Extract IP Address (IPv4)
                    if (sa != null && sa.pointed.sa_family.toInt() == AF_INET) {
                        // Ignore loopback (lo0)
                        if (name != "lo0") {
                            val ipStr = getIpString(sa)

                            // Logic:
                            // - If we haven't found an IP yet, take this one.
                            // - If we found one, but this one is 'en0' (WiFi), overwrite it (WiFi is usually preferred for display).
                            if (foundIp == null || name == "en0") {
                                foundIp = ipStr
                            }
                        }
                    }
                    current = interfaceData.ifa_next
                }
                freeifaddrs(ifap.value)
            }
        }

        return NetworkStatus(
            ip = foundIp ?: "0.0.0.0",
            baseUrl = serverSelectionRepositoryImpl.getCurrentBaseUrl() ?: "",
            isVpnActive = isVpn
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun getIpString(sa: CPointer<sockaddr>): String {
        val buffer = ByteArray(INET_ADDRSTRLEN)

        val ipv4Addr = sa.reinterpret<sockaddr_in>()

        return memScoped {
            val result = inet_ntop(
                AF_INET,
                ipv4Addr.pointed.sin_addr.ptr,
                buffer.refTo(0),
                buffer.size.toUInt()
            )
            result?.toKString() ?: ""
        }
    }
}