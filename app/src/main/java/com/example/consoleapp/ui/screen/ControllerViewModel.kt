package com.example.consoleapp.ui.screen


import android.util.Log
import android.view.KeyEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consoleapp.data.MqttRepository
import com.example.consoleapp.domain.input.JoystickEvent
import com.example.consoleapp.ui.state.ControllerUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

private const val TAG = "ControllerVM"

class ControllerViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(ControllerUiState())
    val uiState: StateFlow<ControllerUiState> = _uiState

    private val mqttRepo = MqttRepository(
        host = "broker.hivemq.com",
        port = 1883,
        clientId = "consoleapp-android"
    )


    private var axisPublishJob: Job? = null

    init {
        Log.d(TAG, "VM init -> broker=${_uiState.value.brokerHost}, topic=${_uiState.value.topic}")
        Log.d(TAG, "Trying to connect to MQTT broker...")

        mqttRepo.connect(
            onConnected = {
                Log.i(TAG, "MQTT connected ✅")
                _uiState.update { it.copy(mqttConnected = true) }
            },
            onDisconnected = {
                Log.w(TAG, "MQTT disconnected ⚠️")
                _uiState.update { it.copy(mqttConnected = false) }
            },
            onError = { err ->
                Log.e(TAG, "MQTT error ❌", err)
                _uiState.update { it.copy(mqttConnected = false) }
            }
        )
    }

    fun onJoystickEvent(event: JoystickEvent) {
        when (event) {
            is JoystickEvent.Axis -> {
                Log.v(TAG, "Axis event -> x=${event.x}, y=${event.y}")
                _uiState.update { it.copy(axisX = event.x.toDouble(), axisY = event.y.toDouble()) }
                scheduleAxisPublish(event.x, event.y)
            }

            is JoystickEvent.Button -> {
                Log.d(TAG, "Button event -> code=${event.code}, pressed=${event.pressed}")
                if (event.code == KeyEvent.KEYCODE_BUTTON_A) {
                    publishData(event.pressed)
                }
            }
        }
    }

    private fun scheduleAxisPublish(x: Float, y: Float) {
        axisPublishJob?.cancel()
        axisPublishJob = viewModelScope.launch {
            delay(40)
            publishAxis(x, y)
        }
    }

    private fun publishAxis(x: Float, y: Float) {
        val topic = _uiState.value.topic
        val payload = """{"x":${"%.3f".format(x)},"y":${"%.3f".format(y)}}"""
        Log.d(TAG, "Publish axis -> topic=$topic payload=$payload")
        mqttRepo.publish(topic, payload.toByteArray(StandardCharsets.UTF_8), qos = 0, retained = false)
    }

    private fun publishData(pressed: Boolean) {
        val topic = _uiState.value.topic
        val payload = """{"data":"A","pressed":$pressed}"""
        Log.d(TAG, "Publish data -> topic=$topic payload=$payload")
        mqttRepo.publish(topic, payload.toByteArray(StandardCharsets.UTF_8), qos = 0, retained = false)
    }

    override fun onCleared() {
        Log.d(TAG, "VM cleared -> disconnecting MQTT")
        super.onCleared()
        mqttRepo.disconnect()
    }
}
