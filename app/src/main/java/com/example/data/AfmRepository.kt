package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AfmRepository(private val dao: AfmDao) {
    val channels: Flow<List<ChannelEntity>> = dao.getAllChannels()
    val callLogs: Flow<List<CallLogEntity>> = dao.getAllCallLogs()
    val aiLogs: Flow<List<AiLogEntity>> = dao.getAllAiLogs()

    fun getMessages(channelId: String): Flow<List<MessageEntity>> = dao.getMessagesForChannel(channelId)

    suspend fun seedInitialData() {
        // We check if we need initial seeding or insert defaults
        val defaultChannels = listOf(
            ChannelEntity("ch_1", "Alpha Relay", 92.5f, 88, true, 4, System.currentTimeMillis()),
            ChannelEntity("ch_2", "Emergency Broadcast", 98.1f, 95, true, 12, System.currentTimeMillis() - 3600000),
            ChannelEntity("ch_3", "Tactical Node", 104.5f, 72, true, 2, System.currentTimeMillis() - 7200000),
            ChannelEntity("ch_4", "Local Peer Mesh", 107.9f, 60, false, 7, System.currentTimeMillis() - 10800000)
        )
        for (ch in defaultChannels) {
            dao.insertChannel(ch)
        }

        // Seed sample messages for ch_1
        val sampleMsgs = listOf(
            MessageEntity(channelId = "ch_1", senderName = "Operator X", content = "AFM radio link established. Signal strength 88%. E2EE active.", timestamp = System.currentTimeMillis() - 60000, isEncrypted = true, isOutgoing = false),
            MessageEntity(channelId = "ch_1", senderName = "You", content = "Acknowledged. Opus low-bandwidth encoder tuned to 12kbps.", timestamp = System.currentTimeMillis() - 30000, isEncrypted = true, isOutgoing = true)
        )
        for (m in sampleMsgs) {
            dao.insertMessage(m)
        }
    }

    suspend fun sendMessage(channelId: String, senderName: String, content: String, isOutgoing: Boolean) {
        val msg = MessageEntity(
            channelId = channelId,
            senderName = senderName,
            content = content,
            timestamp = System.currentTimeMillis(),
            isEncrypted = true,
            isOutgoing = isOutgoing
        )
        dao.insertMessage(msg)
    }

    suspend fun logCall(peerName: String, frequencyMhz: Float, durationSeconds: Int, bitrate: Int, packetLoss: Float, isIncoming: Boolean) {
        val log = CallLogEntity(
            peerName = peerName,
            frequencyMhz = frequencyMhz,
            durationSeconds = durationSeconds,
            timestamp = System.currentTimeMillis(),
            opusBitrateKbps = bitrate,
            packetLossPercent = packetLoss,
            isIncoming = isIncoming
        )
        dao.insertCallLog(log)
    }

    suspend fun logAi(prompt: String, response: String) {
        val log = AiLogEntity(
            prompt = prompt,
            response = response,
            timestamp = System.currentTimeMillis()
        )
        dao.insertAiLog(log)
    }
}
