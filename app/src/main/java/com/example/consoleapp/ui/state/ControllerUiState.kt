package com.example.consoleapp.ui.state

data class ControllerUiState(
    val joystickConnected: Boolean = false,
    val wsConnected: Boolean = false,

    val axisX: Double = 0.0,
    val axisY: Double = 0.0,

    val espHost: String = "192.168.4.1",
    val espPort: Int = 80,
    val wsPath: String = "/RobotArmInput"
)
