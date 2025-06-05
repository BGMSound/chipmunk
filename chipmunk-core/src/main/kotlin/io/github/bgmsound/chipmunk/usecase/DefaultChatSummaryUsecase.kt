package io.github.bgmsound.chipmunk.usecase

import io.github.bgmsound.chipmunk.Chat
import io.github.bgmsound.chipmunk.SummaryResult
import org.springframework.ai.anthropic.AnthropicChatOptions
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Component

@Component
class DefaultChatSummaryUsecase(
    private val chatModel: ChatModel
) : ChatSummaryUsecase {
    override fun summarizeMessage(chats: List<Chat>): SummaryResult {
        val prompt = """
            네 이름은 칩멍크야.
            너는 채팅 내용을 요약하는 역할을 맡고 있어.
            다음 대화를 마크다운 문법을 포함하여 요약해줘.
            
            대화 내용:
            ${chats.joinToString("\n") { "${it.authorName}: ${it.content}" }}
        """.trimIndent()
        println(prompt)
        val response = chatModel.call(Prompt(
            prompt,
            AnthropicChatOptions.builder()
                .model("claude-3-7-sonnet-latest")
                .temperature(0.0)
                .build()
        ))
        println(response.result.output)
        return SummaryResult()
    }
}