package com.example.consoleapp.ui.screen

import androidx.lifecycle.ViewModel
import com.example.consoleapp.ui.state.ControllerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ControllerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ControllerUiState())
    val uiState: StateFlow<ControllerUiState> = _uiState

    fun pressUp() = _uiState.update { it.copy(axisY = 1.0) }
    fun pressDown() = _uiState.update { it.copy(axisY = -1.0) }
    fun pressLeft() = _uiState.update { it.copy(axisX = -1.0) }
    fun pressRight() = _uiState.update { it.copy(axisX = 1.0) }

    fun release() = _uiState.update { it.copy(axisX = 0.0, axisY = 0.0) }
    fun setJoystickConnected(v: Boolean) = _uiState.update { it.copy(joystickConnected = v) }
    fun setMqttConnected(v: Boolean) = _uiState.update { it.copy(mqttConnected = v) }
}
