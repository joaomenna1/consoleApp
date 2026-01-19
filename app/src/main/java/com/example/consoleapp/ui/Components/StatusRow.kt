package com.example.consoleapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusRow(
    joystickConnected: Boolean,
    mqttConnected: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
        StatusChip(
            text = if (joystickConnected) "Joystick Connected" else "Joystick Disconnected",
            ok = joystickConnected,
            modifier = Modifier.weight(1f)
        )
        StatusChip(
            text = if (mqttConnected) "MQTT Connected" else "MQTT Disconnected",
            ok = mqttConnected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatusChip(
    text: String,
    ok: Boolean,
    modifier: Modifier = Modifier
) {
    val bg = if (ok) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer
    val fg = if (ok) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onErrorContainer

    Surface(
        modifier = modifier.height(46.dp),
        shape = RoundedCornerShape(18.dp),
        color = bg,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (ok) Color(0xFF2EE59D) else Color(0xFFFF6B6B))
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = text,
                color = fg,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
        }
    }
}
