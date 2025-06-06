package io.github.bgmsound.chipmunk.usecase

import io.github.bgmsound.chipmunk.Chat
import io.github.bgmsound.chipmunk.SummaryPR
import io.github.bgmsound.chipmunk.GithubSummaryPRCreator
import io.github.bgmsound.chipmunk.LLMChatSummaryAgent
import org.springframework.stereotype.Component

@Component
class DefaultChatSummaryUsecase(
    private val llmChatSummaryAgent: LLMChatSummaryAgent,
    private val prCreator: GithubSummaryPRCreator
) : ChatSummaryUsecase {
    override fun summarizeMessage(chats: List<Chat>): SummaryPR {
        val llmSummary = llmChatSummaryAgent.summarizeMessage(chats)
        return prCreator.createPR(llmSummary)
    }
}