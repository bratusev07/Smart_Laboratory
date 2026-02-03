package ru.bratusev.smartlab.feature_login.models

data class NetworkStatusUi(
    val ip: String,
    val baseUrl: String,
    val isVpnActive: Boolean,
) {
    fun isIpInSameSubnet(): Boolean {
        if (baseUrl != "") {
            val targetIp = extractIpFromUrl(baseUrl) ?: return false
            return areIpsInSameSubnet(this.ip, targetIp)
        }
        return true
    }

    companion object {
        private fun extractIpFromUrl(url: String): String? {
            try {
                val ipv4Pattern =
                    Regex("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)")

                return ipv4Pattern.find(url)?.value
            } catch (e: Exception) {
                return null
            }
        }

        private fun areIpsInSameSubnet(ip1: String, ip2: String): Boolean {
            try {
                val parts1 = ip1.split(".")
                val parts2 = ip2.split(".")

                if (parts1.size != 4 || parts2.size != 4) return false

                return parts1[0] == parts2[0] && parts1[1] == parts2[1] && parts1[2] == parts2[2]
            } catch (e: Exception) {
                return false
            }
        }
    }
}