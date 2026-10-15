package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.RadioState
import com.example.ui.components.AudioWaveform
import com.example.ui.theme.*

@Composable
fun VoiceCallScreen(
    radioState: RadioState,
    onSetBitrate: (Int) -> Unit,
    onToggleFec: (Boolean) -> Unit,
    onToggleDtx: (Boolean) -> Unit,
    onEndCall: () -> Unit
) {
    var callDurationSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            callDurationSeconds++
        }
    }

    val minutes = callDurationSeconds / 60
    val seconds = callDurationSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Header
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = RadioAmber, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("E2EE SECURE VOICE CALL", style = MaterialTheme.typography.labelMedium, color = RadioAmber, letterSpacing = 2.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Frequency: ${radioState.frequencyMhz} MHz", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(timeFormatted, style = MaterialTheme.typography.headlineMedium, color = TextSecondary)
        }

        // Center Waveform & Telemetry
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
                    .border(2.dp, RadioAmber, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Mic, contentDescription = null, tint = RadioAmber, modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            AudioWaveform(height = 80.dp, barColor = RadioAmber)

            Spacer(modifier = Modifier.height(24.dp))

            // Opus & Congestion Control Metrics Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Opus Low-Bandwidth Parameters", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Bitrate: ${radioState.opusBitrateKbps} kbps", color = TextSecondary, fontSize = 13.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onSetBitrate(12) },
                                colors = ButtonDefaults.buttonColors(containerColor = if (radioState.opusBitrateKbps == 12) RadioAmber else DarkSurfaceVariant),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) { Text("12k", fontSize = 12.sp, color = if (radioState.opusBitrateKbps == 12) DarkBackground else TextPrimary) }

                            Button(
                                onClick = { onSetBitrate(24) },
                                colors = ButtonDefaults.buttonColors(containerColor = if (radioState.opusBitrateKbps == 24) RadioAmber else DarkSurfaceVariant),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) { Text("24k", fontSize = 12.sp, color = if (radioState.opusBitrateKbps == 24) DarkBackground else TextPrimary) }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Forward Error Correction (FEC)", color = TextSecondary, fontSize = 13.sp)
                        Switch(
                            checked = radioState.fecEnabled,
                            onCheckedChange = onToggleFec,
                            colors = SwitchDefaults.colors(checkedThumbColor = RadioAmber, checkedTrackColor = RadioAmberDim)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Discontinuous Transmission (DTX)", color = TextSecondary, fontSize = 13.sp)
                        Switch(
                            checked = radioState.dtxEnabled,
                            onCheckedChange = onToggleDtx,
                            colors = SwitchDefaults.colors(checkedThumbColor = RadioAmber, checkedTrackColor = RadioAmberDim)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Latency: ${radioState.latencyMs} ms • Loss: ${(radioState.congestionLevel * 100).toInt()}%", color = SignalGreen, fontSize = 12.sp)
                }
            }
        }

        // Bottom Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onEndCall,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                shape = CircleShape,
                modifier = Modifier
                    .size(64.dp)
                    .testTag("end_call_button")
            ) {
                Icon(Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White)
            }
        }
    }
}
