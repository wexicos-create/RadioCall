package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.RadioState
import com.example.data.ChannelEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    radioState: RadioState,
    channels: List<ChannelEntity>,
    onTuneFrequency: (Float) -> Unit,
    onSelectChannel: (ChannelEntity) -> Unit,
    onStartCall: () -> Unit
) {
    var sliderVal by remember(radioState.frequencyMhz) { mutableStateOf(radioState.frequencyMhz) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        // Editorial Header
        Text(
            text = "AFM SECURE TRANSCEIVER",
            style = MaterialTheme.typography.labelMedium,
            color = RadioAmber,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Offline Radio Frequency Grid",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Frequency Dial Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Radio, contentDescription = null, tint = RadioAmber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AM/FM Modulation (AFM)", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Badge(containerColor = SignalGreen.copy(alpha = 0.2f)) {
                        Text("${radioState.signalStrength}% Signal", color = SignalGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Frequency Display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%.1f MHz", sliderVal),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = RadioAmber,
                            modifier = Modifier.testTag("frequency_display")
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Opus Codec • ${radioState.opusBitrateKbps} kbps • E2EE Active",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Slider
                Slider(
                    value = sliderVal,
                    onValueChange = { sliderVal = it },
                    onValueChangeFinished = { onTuneFrequency(sliderVal) },
                    valueRange = 87.5f..108.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = RadioAmber,
                        activeTrackColor = RadioAmber,
                        inactiveTrackColor = BorderColor
                    ),
                    modifier = Modifier.testTag("frequency_slider")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onStartCall,
                        colors = ButtonDefaults.buttonColors(containerColor = RadioAmber, contentColor = DarkBackground),
                        modifier = Modifier.weight(1f).height(48.dp).testTag("start_call_button")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Start Call", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "SECURE FREQUENCY CHANNELS",
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(channels) { channel ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, BorderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectChannel(channel) }
                        .testTag("channel_card_${channel.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = RadioAmber, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(channel.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${channel.frequencyMhz} MHz • ${channel.peerCount} nodes active", color = TextSecondary, fontSize = 13.sp)
                            }
                        }
                        Icon(Icons.Default.Send, contentDescription = null, tint = TextSecondary)
                    }
                }
            }
        }
    }
}
