package com.example.consoleapp.ui.state

data class ControllerUiState(
    val joystickConnected: Boolean = true,
    val mqttConnected: Boolean = true,
    val axisX: Double = 0.0,
    val axisY: Double = 0.0,
    val broker: String = "path-broker",
    val topic: String = "arm/cmd"
)