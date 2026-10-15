package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.CryptoManager
import com.example.data.ChannelEntity
import com.example.data.MessageEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    channel: ChannelEntity,
    messages: List<MessageEntity>,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit
) {
    var textInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Column {
                    Text(channel.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = RadioAmber, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("E2EE Encrypted • ${channel.frequencyMhz} MHz", color = RadioAmber, fontSize = 12.sp)
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
        )

        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messages) { msg ->
                val decryptedContent = if (msg.isEncrypted) CryptoManager.decrypt(msg.content) else msg.content
                val isOutgoing = msg.isOutgoing

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isOutgoing) DarkSurfaceVariant else DarkSurface,
                        border = BorderStroke(1.dp, BorderColor),
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .testTag("message_bubble_${msg.id}")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = msg.senderName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = RadioAmber,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = "Encrypted",
                                    tint = RadioAmber,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = decryptedContent,
                                color = TextPrimary,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // Input bar
        Surface(
            color = DarkSurface,
            border = BorderStroke(1.dp, BorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Secure message...", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadioAmber,
                        unfocusedBorderColor = BorderColor,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input")
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            val encrypted = CryptoManager.encrypt(textInput)
                            onSendMessage(encrypted)
                            textInput = ""
                        }
                    },
                    modifier = Modifier
                        .background(RadioAmber, RoundedCornerShape(8.dp))
                        .testTag("send_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = DarkBackground)
                }
            }
        }
    }
}
