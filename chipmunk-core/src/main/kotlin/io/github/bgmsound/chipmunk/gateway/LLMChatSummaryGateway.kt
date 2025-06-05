package io.github.bgmsound.chipmunk.gateway

import io.github.bgmsound.chipmunk.Chat
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value

@Component
class LLMChatSummaryGateway(
    @Value("\${base-prompt}") private val basePrompt: String
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun summarizeMessage(chats: List<Chat>): LLMSummary {
        val conversation = StringBuilder().apply {
            chats.forEach { chat ->
                append("- **${chat.authorName}**: ${chat.content}\n")
            }
        }
        val prompt = basePrompt.replace("{conversation}", conversation.toString())
        logger.info(prompt)
        return LLMSummary()
    }
}