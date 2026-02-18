package ru.bratusev.smartlab.ui.core.models

data class ServerSelectionUi(
    val serverList: List<ServerInfoUi>,
    val currentServer: ServerInfoUi?,
    val expanded: Boolean
) {
    data class ServerInfoUi(
        val url: String,
        val name: String,
        val login: String,
        val password: String
    )
}
