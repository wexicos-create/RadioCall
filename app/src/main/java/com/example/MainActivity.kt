package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.core.AfmRadioEngine
import com.example.data.AfmDatabase
import com.example.data.AfmRepository
import com.example.data.ChannelEntity
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

enum class Screen {
    Dashboard, Chat, VoiceCall, Vault, Settings
}

class MainActivity : ComponentActivity() {
    private val radioEngine = AfmRadioEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AfmDatabase.getDatabase(applicationContext)
        val repository = AfmRepository(db.afmDao())

        lifecycleScope.launch {
            repository.seedInitialData()
        }

        setContent {
            MyApplicationTheme {
                var currentScreen by remember { mutableStateOf(Screen.Dashboard) }
                val radioState by radioEngine.state.collectAsState()
                val channels by repository.channels.collectAsState(initial = emptyList())
                val callLogs by repository.callLogs.collectAsState(initial = emptyList())

                var selectedChannel by remember { mutableStateOf<ChannelEntity?>(null) }
                val messages by remember(selectedChannel) {
                    if (selectedChannel != null) {
                        repository.getMessages(selectedChannel!!.id)
                    } else {
                        kotlinx.coroutines.flow.flowOf(emptyList())
                    }
                }.collectAsState(initial = emptyList())

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentScreen != Screen.VoiceCall && currentScreen != Screen.Chat) {
                            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Dashboard,
                                    onClick = { currentScreen = Screen.Dashboard },
                                    icon = { Icon(Icons.Default.Radio, contentDescription = "Radio") },
                                    label = { Text("Radio") },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Vault,
                                    onClick = { currentScreen = Screen.Vault },
                                    icon = { Icon(Icons.Default.Storage, contentDescription = "Vault") },
                                    label = { Text("Vault") },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                                NavigationBarItem(
                                    selected = currentScreen == Screen.Settings,
                                    onClick = { currentScreen = Screen.Settings },
                                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                    label = { Text("Settings") },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            Screen.Dashboard -> DashboardScreen(
                                radioState = radioState,
                                channels = channels,
                                onTuneFrequency = { radioEngine.tuneFrequency(it) },
                                onSelectChannel = { ch ->
                                    selectedChannel = ch
                                    currentScreen = Screen.Chat
                                },
                                onStartCall = {
                                    currentScreen = Screen.VoiceCall
                                }
                            )
                            Screen.Chat -> ChatScreen(
                                channel = selectedChannel ?: channels.firstOrNull() ?: ChannelEntity("default", "Default", 94.5f, 80, true, 1, 0L),
                                messages = messages,
                                onBack = { currentScreen = Screen.Dashboard },
                                onSendMessage = { content ->
                                    lifecycleScope.launch {
                                        val chId = selectedChannel?.id ?: "ch_1"
                                        repository.sendMessage(chId, "You", content, true)
                                        // Offline AI / Auto peer reply
                                        kotlinx.coroutines.delay(1000)
                                        repository.sendMessage(chId, "Peer Node", "ACK received over AFM frequency. E2EE verified.", false)
                                    }
                                }
                            )
                            Screen.VoiceCall -> VoiceCallScreen(
                                radioState = radioState,
                                onSetBitrate = { radioEngine.setOpusBitrate(it) },
                                onToggleFec = { radioEngine.toggleFec(it) },
                                onToggleDtx = { radioEngine.toggleDtx(it) },
                                onEndCall = {
                                    lifecycleScope.launch {
                                        repository.logCall("Peer Node ${radioState.frequencyMhz}MHz", radioState.frequencyMhz, 45, radioState.opusBitrateKbps, radioState.congestionLevel, true)
                                    }
                                    currentScreen = Screen.Dashboard
                                }
                            )
                            Screen.Vault -> VaultScreen(callLogs = callLogs)
                            Screen.Settings -> SettingsScreen(radioState = radioState)
                        }
                    }
                }
            }
        }
    }
}
