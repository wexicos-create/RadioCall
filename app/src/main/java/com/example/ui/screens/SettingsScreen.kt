package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.RadioState
import com.example.ui.theme.*

@Composable
fun SettingsScreen(radioState: RadioState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "SYSTEM CONFIGURATION",
            style = MaterialTheme.typography.labelMedium,
            color = RadioAmber,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Radio & Security Diagnostics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = RadioAmber)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("End-to-End Encryption", fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("AES-256 GCM Key Exchange Verified", color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = RadioAmber)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("AFM Transceiver Protocol", fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Frequency Range: 87.5 - 108.0 MHz\nOpus Codec Target: ${radioState.opusBitrateKbps} kbps\nZero Network / Internet Required", color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
