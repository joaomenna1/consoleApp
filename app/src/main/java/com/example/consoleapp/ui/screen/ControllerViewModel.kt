package com.example.consoleapp.ui.screen

import android.util.Log
import android.view.KeyEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consoleapp.data.WebSocketRepository
import com.example.consoleapp.domain.input.JoystickEvent
import com.example.consoleapp.types.Action
import com.example.consoleapp.types.Part
import com.example.consoleapp.ui.state.ControllerUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "ControllerVM"

class ControllerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ControllerUiState())
    val uiState: StateFlow<ControllerUiState> = _uiState

    private val wsRepo = WebSocketRepository(
        host = _uiState.value.espHost,
        port = _uiState.value.espPort,
        path = _uiState.value.wsPath
    )

    // Para “segurar botão” e repetir comando:
    private var repeatJob: Job? = null

    init {
        Log.d(TAG, "Connecting WS...")
        wsRepo.connect(
            onConnected = { _uiState.update { it.copy(wsConnected = true) } },
            onDisconnected = { _uiState.update { it.copy(wsConnected = false) } },
            onError = { err ->
                Log.e(TAG, "WS error", err)
                _uiState.update { it.copy(wsConnected = false) }
            }
        )
    }

    fun onJoystickEvent(event: JoystickEvent) {
        when (event) {
            is JoystickEvent.Axis -> {

                _uiState.update { it.copy(axisX = event.x.toDouble(), axisY = event.y.toDouble()) }
                // quando o joystick estiver pronto no driver, ja tem essa base
            }

            is JoystickEvent.Button -> {
                handleButton(event.code, event.pressed)
            }
        }
    }

    private fun send(part: Part, action: Action) {
        wsRepo.sendCommand(part.value, action.value)
    }

    private fun handleButton(code: Int, pressed: Boolean) {
        when (code) {

            KeyEvent.KEYCODE_DPAD_LEFT ->
                repeatWhilePressed(pressed) { send(Part.Base, Action.LEFT) }

            KeyEvent.KEYCODE_DPAD_RIGHT ->
                repeatWhilePressed(pressed) { send(Part.Base, Action.RIGHT) }

            KeyEvent.KEYCODE_DPAD_UP ->
                repeatWhilePressed(pressed) { send(Part.Shoulder, Action.UP) }

            KeyEvent.KEYCODE_DPAD_DOWN ->
                repeatWhilePressed(pressed) { send(Part.Shoulder, Action.DOWN) }

            KeyEvent.KEYCODE_BUTTON_L1 ->
                repeatWhilePressed(pressed) { send(Part.Elbow, Action.DOWN) }

            KeyEvent.KEYCODE_BUTTON_R1 ->
                repeatWhilePressed(pressed) { send(Part.Elbow, Action.UP) }

            KeyEvent.KEYCODE_BUTTON_A ->
                if (pressed) send(Part.Gripper, Action.CLOSE)

            KeyEvent.KEYCODE_BUTTON_B ->
                if (pressed) send(Part.Gripper, Action.OPEN)

            KeyEvent.KEYCODE_BUTTON_START ->
                if (pressed) send(Part.Home, Action.GO)

            KeyEvent.KEYCODE_BUTTON_SELECT ->
                if (pressed) toggleDemo()
        }
    }


    private var demoOn = false
    private fun toggleDemo() {
        demoOn = !demoOn
        send(Part.Demo, if (demoOn) Action.ON else Action.OFF)
    }

    private fun repeatWhilePressed(pressed: Boolean, send: () -> Unit) {
        if (pressed) {
            if (repeatJob?.isActive == true) return
            repeatJob = viewModelScope.launch {

                send()

                while (true) {
                    delay(90)
                    send()
                }
            }
        } else {
            repeatJob?.cancel()
            repeatJob = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        wsRepo.disconnect()
    }
}
