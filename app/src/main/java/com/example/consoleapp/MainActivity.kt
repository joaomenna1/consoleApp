package com.example.consoleapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.consoleapp.ui.theme.ConsoleAppTheme
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.HorizontalDivider


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConsoleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ControllerScreen()
                }
            }
        }
    }
}

@Composable
private fun ControllerScreen() {
    // por enquanto ta fake mas so pra ver os botoes funcionando
    var joystickConnected by remember { mutableStateOf(true) }
    var mqttConnected by remember { mutableStateOf(true) }

    var axisX by remember { mutableStateOf(0.00) }
    var axisY by remember { mutableStateOf(0.00) }

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
                // isso so caso precise mesmo
            }
        )

        StatusRow(
            joystickConnected = joystickConnected,
            mqttConnected = mqttConnected
        )

        TelemetryCard(axisX = axisX, axisY = axisY)

        ControlPadCard(
            onUp = { axisY = 1.00 },
            onDown = { axisY = -1.00 },
            onLeft = { axisX = -1.00 },
            onRight = { axisX = 1.00 },
            onRelease = {
                axisX = 0.00
                axisY = 0.00
            }
        )

        BrokerFooter(
            broker = "path-broker",
            topic = "arm/cmd"
        )
    }
}

@Composable
private fun TopHeader(
    title: String,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
            )

        }
    }
}


@Composable
private fun StatusRow(
    joystickConnected: Boolean,
    mqttConnected: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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

@Composable
private fun TelemetryCard(axisX: Double, axisY: Double) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "TELEMETRY",
                fontSize = 12.sp,
                letterSpacing = 1.2.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TelemetryValue(label = "AXIS_X", value = axisX)
                TelemetryValue(label = "AXIS_Y", value = axisY)
            }

            Spacer(Modifier.height(12.dp))

            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "D-pad only",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                val iconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

                Icon(Icons.Filled.ArrowUpward, contentDescription = "Up", tint = iconColor, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Icon(Icons.Filled.ArrowDownward, contentDescription = "Down", tint = iconColor, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Icon(Icons.Filled.ArrowBack, contentDescription = "Left", tint = iconColor, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Icon(Icons.Filled.ArrowForward, contentDescription = "Right", tint = iconColor, modifier = Modifier.size(16.dp))
            }

        }
    }
}

@Composable
private fun TelemetryValue(label: String, value: Double) {
    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = String.format("%.2f", value),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ControlPadCard(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onRelease: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 2.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            DPad(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(190.dp),
                onUp = onUp,
                onDown = onDown,
                onLeft = onLeft,
                onRight = onRight,
                onRelease = onRelease
            )
        }
    }
}

@Composable
private fun DPad(
    modifier: Modifier = Modifier,
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onRelease: () -> Unit
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DPadButton(icon = Icons.Filled.ArrowUpward, onPress = onUp, onRelease = onRelease)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            DPadButton(icon = Icons.Filled.ArrowBack, onPress = onLeft, onRelease = onRelease)

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
                        shape = CircleShape
                    )
            )

            DPadButton(icon = Icons.Filled.ArrowForward, onPress = onRight, onRelease = onRelease)
        }

        DPadButton(icon = Icons.Filled.ArrowDownward, onPress = onDown, onRelease = onRelease)
    }
}

@Composable
private fun DPadButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onPress: () -> Unit,
    onRelease: () -> Unit
) {

    FilledTonalButton(
        onClick = {
            onPress()
        },
        modifier = Modifier.size(64.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Icon(imageVector = icon, contentDescription = null)
    }
}

@Composable
private fun BrokerFooter(broker: String, topic: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MQTT Broker: $broker",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
            Text(
                text = "topic: $topic",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
            )
        }
    }
}
