package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channels")
data class ChannelEntity(
    @PrimaryKey val id: String,
    val name: String,
    val frequencyMhz: Float,
    val signalStrength: Int, // 0 to 100
    val isSecure: Boolean,
    val peerCount: Int,
    val lastActiveTime: Long
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val channelId: String,
    val senderName: String,
    val content: String,
    val timestamp: Long,
    val isEncrypted: Boolean,
    val isOutgoing: Boolean,
    val audioDurationSeconds: Int = 0
)

@Entity(tableName = "call_logs")
data class CallLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val peerName: String,
    val frequencyMhz: Float,
    val durationSeconds: Int,
    val timestamp: Long,
    val opusBitrateKbps: Int,
    val packetLossPercent: Float,
    val isIncoming: Boolean
)

@Entity(tableName = "ai_logs")
data class AiLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val prompt: String,
    val response: String,
    val timestamp: Long
)
