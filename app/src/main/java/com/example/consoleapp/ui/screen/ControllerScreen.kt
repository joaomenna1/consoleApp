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
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ControllerScreen(
    vm: ControllerViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            onUp = vm::pressUp,
            onDown = vm::pressDown,
            onLeft = vm::pressLeft,
            onRight = vm::pressRight,
            onRelease = vm::release
        )

        Footer(
            broker = state.broker,
            topic = state.topic
        )
    }
}
