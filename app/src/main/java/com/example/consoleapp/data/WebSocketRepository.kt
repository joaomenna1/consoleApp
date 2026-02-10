package com.example.consoleapp.data

import android.util.Log
import okhttp3.*
import okio.ByteString
import java.util.concurrent.atomic.AtomicBoolean

private const val TAG = "WS"

class WebSocketRepository(
    private val host: String,
    private val port: Int = 80,
    private val path: String = "/RobotArmInput"
) {
    private val client = OkHttpClient()

    private var socket: WebSocket? = null
    private val _isConnected = AtomicBoolean(false)
    val isConnected: Boolean get() = _isConnected.get()

    fun connect(
        onConnected: (() -> Unit)? = null,
        onDisconnected: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        val url = HttpUrl.Builder()
            .scheme("ws")
            .host(host)
            .port(port)
            .encodedPath(path)
            .build()

        Log.d(TAG, "Connecting -> $url")

        val req = Request.Builder().url(url).build()
        socket = client.newWebSocket(req, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _isConnected.set(true)
                Log.i(TAG, "Connected ✅")
                onConnected?.invoke()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Message <- $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d(TAG, "Message <- bytes(${bytes.size})")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.w(TAG, "Closing: $code $reason")
                webSocket.close(code, reason)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _isConnected.set(false)
                Log.w(TAG, "Closed: $code $reason")
                onDisconnected?.invoke()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _isConnected.set(false)
                Log.e(TAG, "Failure", t)
                onDisconnected?.invoke()
                onError?.invoke(t)
            }
        })
    }

    fun sendCommand(part: String, action: String): Boolean {
        if (!isConnected) {
            Log.w(TAG, "Send skipped (not connected)")
            return false
        }
        val msg = "$part,$action"
        Log.d(TAG, "Send -> $msg")
        return socket?.send(msg) == true
    }

    fun disconnect() {
        try {
            socket?.close(1000, "bye")
        } catch (t: Throwable) {
            Log.e(TAG, "Disconnect error", t)
        } finally {
            _isConnected.set(false)
            socket = null
        }
    }
}
