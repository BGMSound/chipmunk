package io.github.bgmsound.chipmunk.usecase

import io.github.bgmsound.chipmunk.Chat
import io.github.bgmsound.chipmunk.SummaryResult
import io.github.bgmsound.chipmunk.gateway.GithubPRCreator
import io.github.bgmsound.chipmunk.gateway.LLMChatSummaryGateway
import org.springframework.stereotype.Component

@Component
class DefaultChatSummaryUsecase(
    private val llmChatSummaryGateway: LLMChatSummaryGateway,
    private val githubPRCreator: GithubPRCreator
) : ChatSummaryUsecase {
    override fun summarizeMessage(chats: List<Chat>): SummaryResult {
        val llmSummary = llmChatSummaryGateway.summarizeMessage(chats)
        githubPRCreator.createPR(llmSummary)
        return SummaryResult()
    }
}