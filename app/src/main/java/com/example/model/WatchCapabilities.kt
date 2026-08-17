package com.example.model

data class WatchCapabilities(
    var bluetoothClassic: Boolean? = null,
    var bluetoothLE: Boolean? = null,
    var batteryExposed: Boolean? = null,
    var notificationsSupported: Boolean? = null,
    var replySupported: Boolean? = null,
    var microphone: String? = null,
    var heartRate: Boolean? = null,
    var steps: Boolean? = null,
    var gps: Boolean? = null,
    var nfc: Boolean? = null
)
