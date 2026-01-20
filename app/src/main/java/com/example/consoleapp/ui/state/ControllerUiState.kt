package com.example.consoleapp.ui.state

data class ControllerUiState(
    val joystickConnected: Boolean = false,
    val mqttConnected: Boolean = false,
    val axisX: Double = 0.0,
    val axisY: Double = 0.0,
    val brokerHost: String = "broker.hivemq.com",
    val brokerPort: Int = 1883,
    val topic: String = "arm/cmd"
)