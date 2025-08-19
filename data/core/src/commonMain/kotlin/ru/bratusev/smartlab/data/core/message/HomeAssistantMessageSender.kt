package ru.bratusev.smartlab.data.core.message

interface HomeAssistantMessageSender {
    fun updateSwitchState(switchId: String, switchState: String)
}