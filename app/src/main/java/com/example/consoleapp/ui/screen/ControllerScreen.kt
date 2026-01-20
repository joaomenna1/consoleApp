package com.example.consoleapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.consoleapp.ui.components.Footer
import com.example.consoleapp.ui.components.ControlPadCard
import com.example.consoleapp.ui.components.StatusRow
import com.example.consoleapp.ui.components.TelemetryCard
import com.example.consoleapp.ui.components.TopHeader
import com.example.consoleapp.domain.input.JoystickEvent
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme



@Composable
fun ControllerScreen(
    vm: ControllerViewModel
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // ✅ garante fundo escuro
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TopHeader(
            title = "Console App",
            onSettingsClick = {

            }
        )

        StatusRow(
            joystickConnected = state.joystickConnected,
            mqttConnected = state.mqttConnected
        )

        TelemetryCard(axisX = state.axisX, axisY = state.axisY)

        ControlPadCard(
            onUp = { vm.onJoystickEvent(JoystickEvent.Axis(x = 0f, y = 1f)) },
            onDown = { vm.onJoystickEvent(JoystickEvent.Axis(x = 0f, y = -1f)) },
            onLeft = { vm.onJoystickEvent(JoystickEvent.Axis(x = -1f, y = 0f)) },
            onRight = { vm.onJoystickEvent(JoystickEvent.Axis(x = 1f, y = 0f)) },
            onRelease = { vm.onJoystickEvent(JoystickEvent.Axis(x = 0f, y = 0f)) }
        )

        Footer(
            broker = state.brokerHost,
            topic = state.topic
        )
    }
}
