package com.example.consoleapp

import android.os.Bundle
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.consoleapp.domain.input.JoystickEvent
import com.example.consoleapp.ui.screen.ControllerScreen
import com.example.consoleapp.ui.screen.ControllerViewModel
import com.example.consoleapp.ui.theme.ConsoleAppTheme
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier


class MainActivity : ComponentActivity() {

    private val vm: ControllerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ConsoleAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ControllerScreen(vm = vm)
                }
            }
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.isFromJoystickLikeDevice()) {
            vm.onJoystickEvent(JoystickEvent.Button(keyCode, pressed = true))
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (event.isFromJoystickLikeDevice()) {
            vm.onJoystickEvent(JoystickEvent.Button(keyCode, pressed = false))
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        if (event.isFromJoystickLikeDevice() && event.action == MotionEvent.ACTION_MOVE) {
            val x = event.getCenteredAxis(MotionEvent.AXIS_X)
            val y = event.getCenteredAxis(MotionEvent.AXIS_Y)
            vm.onJoystickEvent(JoystickEvent.Axis(x, y))
            return true
        }
        return super.onGenericMotionEvent(event)
    }
}

private fun KeyEvent.isFromJoystickLikeDevice(): Boolean {
    return (source and InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK ||
            (source and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD ||
            (source and InputDevice.SOURCE_DPAD) == InputDevice.SOURCE_DPAD
}

private fun MotionEvent.isFromJoystickLikeDevice(): Boolean {
    return (source and InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK ||
            (source and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD
}


private fun MotionEvent.getCenteredAxis(axis: Int): Float {
    val device = device ?: return 0f
    val range = device.getMotionRange(axis, source) ?: return 0f
    val value = getAxisValue(axis)

    val flat = range.flat
    return if (kotlin.math.abs(value) > flat) value else 0f
}
