package com.example.consoleapp.domain.input

sealed class JoystickEvent {
    data class Axis(val x: Float, val y: Float) : JoystickEvent()
    data class Button(val code: Int, val pressed: Boolean) : JoystickEvent()
}
