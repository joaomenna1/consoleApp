package com.example.consoleapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.remember
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ControlPadCard(
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
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        DPadButton(icon = Icons.Filled.ArrowUpward, onPress = onUp, onRelease = onRelease)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
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
    icon: ImageVector,
    onPress: () -> Unit,
    onRelease: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    FilledTonalButton(
        onClick = {},
        modifier = Modifier
            .size(64.dp)
            .pressReleaseGesture(
                onPress = onPress,
                onRelease = onRelease
            ),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        interactionSource = interactionSource
    ) {
        Icon(imageVector = icon, contentDescription = null)
    }
}

@SuppressLint("UnnecessaryComposedModifier")
private fun Modifier.pressReleaseGesture(
    onPress: () -> Unit,
    onRelease: () -> Unit
): Modifier = composed {
    pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                onPress()
                val released = tryAwaitRelease()
                onRelease()
            }
        )
    }
}