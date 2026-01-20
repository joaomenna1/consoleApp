package com.example.consoleapp.data

import android.util.Log
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import java.nio.charset.StandardCharsets

private const val TAG = "MQTT"

class MqttRepository(
    host: String,
    port: Int,
    clientId: String
) {
    private val client: Mqtt3AsyncClient =
        MqttClient.builder()
            .useMqttVersion3()
            .identifier(clientId)
            .serverHost(host)
            .serverPort(port)
            .buildAsync()

    @Volatile
    var isConnected: Boolean = false
        private set

    fun connect(
        onConnected: (() -> Unit)? = null,
        onDisconnected: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null
    ) {
        Log.d(TAG, "Connecting to ...")

        client.connect()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    isConnected = false
                    Log.e(TAG, "Connect failed", throwable)
                    onDisconnected?.invoke()
                    onError?.invoke(throwable)
                } else {
                    isConnected = true
                    Log.i(TAG, "Connected ✅")
                    onConnected?.invoke()

                    // opcional: logar desconexões
                    client.publishes(MqttGlobalPublishFilter.ALL) { /* ignore */ }
                }
            }
    }

    fun publish(
        topic: String,
        payload: ByteArray,
        qos: Int = 0,
        retained: Boolean = false
    ) {
        if (!isConnected) {
            Log.w(TAG, "Publish skipped (not connected)")
            return
        }

        val qosEnum = when (qos) {
            0 -> MqttQos.AT_MOST_ONCE
            1 -> MqttQos.AT_LEAST_ONCE
            else -> MqttQos.EXACTLY_ONCE
        }

        val payloadStr = String(payload, StandardCharsets.UTF_8)
        Log.d(TAG, "Publish -> topic=$topic payload=$payloadStr")

        client.publishWith()
            .topic(topic)
            .qos(qosEnum)
            .payload(payload)
            .retain(retained)
            .send()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    Log.e(TAG, "Publish failed", throwable)
                }
            }
    }

    fun disconnect() {
        try {
            client.disconnect()
            isConnected = false
            Log.d(TAG, "Disconnected")
        } catch (t: Throwable) {
            Log.e(TAG, "Disconnect error", t)
        }
    }
}
