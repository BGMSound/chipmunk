package io.github.bgmsound.chipmunk.handler

import io.github.bgmsound.chipmunk.usecase.ChatSummaryUsecase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GuildChatSummationRequestListener(
    @Value("\${app.message.discord.summary-reaction-code}") private val summaryReactionCode: String,
    @Value("\${app.message.discord.summary-message}") private val summaryMessage: String,
    @Value("\${app.message.discord.summary-complete-message}") private val summaryCompleteMessage: String,

    private val coroutineScope: CoroutineScope,
    private val messageFetcher: DiscordReactionMessageFetcher,

    private val chatSummaryUsecase: ChatSummaryUsecase,
) : ListenerAdapter(), CoroutineScope by coroutineScope {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        if (!event.channelType.isGuild) {
            return
        }
        if (event.emoji.asReactionCode != summaryReactionCode) {
            println(event.emoji.asReactionCode)
            return
        }
        if (event.user?.isBot == true) {
            return
        }
        val mention = event.user?.asMention
        val channel = event.channel

        mention?.let { mention ->
            channel.sendMessage(summaryMessage.replace("{mention}", mention)).queue()
        }
        coroutineScope.launch {
            val messages = async {
                val messages = messageFetcher.fetchMessageUntil(event.messageId, channel)
                if (messages.isEmpty()) {
                    logger.warn("No messages found for reaction in channel ${channel.id} by user ${event.user?.id}")
                    return@async emptyList()
                }
                return@async messages
            }.await()
            if (messages.isEmpty()) {
                return@launch
            }
            launch {
                val result = chatSummaryUsecase.summarizeMessage(messages)
                mention?.let { mention ->
                    channel.sendMessage(
                        summaryCompleteMessage
                            .replace("{message-size}", messages.size.toString())
                            .replace("{mention}", mention)
                            .replace("{link}", "[${result.title} #${result.id.value}](${result.url})")
                    ).queue()
                }
            }
        }
    }
}