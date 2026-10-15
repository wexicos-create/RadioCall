package com.example.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

data class RadioState(
    val frequencyMhz: Float = 94.5f,
    val isTransmitting: Boolean = false,
    val isReceiving: Boolean = false,
    val signalStrength: Int = 85,
    val opusBitrateKbps: Int = 16, // Low-bandwidth optimized default
    val fecEnabled: Boolean = true,
    val dtxEnabled: Boolean = true,
    val congestionLevel: Float = 0.05f, // 5% packet loss simulated
    val latencyMs: Int = 38
)

class AfmRadioEngine {
    private val _state = MutableStateFlow(RadioState())
    val state: StateFlow<RadioState> = _state.asStateFlow()

    fun tuneFrequency(newFrequency: Float) {
        val clamped = newFrequency.coerceIn(87.5f, 108.0f)
        val simulatedStrength = Random.nextInt(55, 99)
        val simulatedCongestion = Random.nextFloat() * 0.1f
        val simulatedLatency = Random.nextInt(25, 75)
        _state.value = _state.value.copy(
            frequencyMhz = clamped,
            signalStrength = simulatedStrength,
            congestionLevel = simulatedCongestion,
            latencyMs = simulatedLatency
        )
    }

    fun setOpusBitrate(bitrate: Int) {
        _state.value = _state.value.copy(opusBitrateKbps = bitrate.coerceIn(6, 32))
    }

    fun toggleFec(enabled: Boolean) {
        _state.value = _state.value.copy(fecEnabled = enabled)
    }

    fun toggleDtx(enabled: Boolean) {
        _state.value = _state.value.copy(dtxEnabled = enabled)
    }

    fun startTransmission() {
        _state.value = _state.value.copy(isTransmitting = true, isReceiving = false)
    }

    fun stopTransmission() {
        _state.value = _state.value.copy(isTransmitting = false)
    }

    fun startReceiving() {
        _state.value = _state.value.copy(isReceiving = true, isTransmitting = false)
    }

    fun stopReceiving() {
        _state.value = _state.value.copy(isReceiving = false)
    }
}
