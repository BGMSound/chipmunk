package io.github.bgmsound.chipmunk.handler

import io.github.bgmsound.chipmunk.Chat
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import org.springframework.stereotype.Component

@Component
class DiscordReactionMessageFetcher {
    suspend fun fetchMessageUntil(messageId: String, channel: MessageChannel): List<Chat> {
        val messages = mutableListOf<Message>()
        if (channel is ThreadChannel) {
            val parentMessage = channel.retrieveParentMessage().complete()
            if (parentMessage != null && !messages.contains(parentMessage)) {
                messages.add(parentMessage)
            }
        }

        channel.getHistoryBefore(messageId, 100)
            .complete().retrievedHistory
            .filter { !it.author.isBot && !it.contentRaw.isEmpty() }.let { pastMessages ->
                messages.addAll(pastMessages.reversed())
            }

        val reactedMessage = channel.retrieveMessageById(messageId).complete()
        messages.add(reactedMessage)

        return messages.map { it.toChat() }
    }

    private fun Message.toChat() = Chat(
        id = this.id,
        authorName = author.name,
        content = contentRaw,
        timestamp = timeCreated.toLocalDateTime()
    )
}