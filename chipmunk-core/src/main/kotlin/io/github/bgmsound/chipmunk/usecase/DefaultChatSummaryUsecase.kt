package io.github.bgmsound.chipmunk.usecase

import io.github.bgmsound.chipmunk.Chat
import io.github.bgmsound.chipmunk.SummaryPR
import io.github.bgmsound.chipmunk.GithubSummaryPRCreator
import io.github.bgmsound.chipmunk.LLMChatSummaryAgent
import io.github.bgmsound.chipmunk.Topic
import io.github.bgmsound.chipmunk.TopicRepository
import org.springframework.stereotype.Component

@Component
class DefaultChatSummaryUsecase(
    private val llmChatSummaryAgent: LLMChatSummaryAgent,
    private val prCreator: GithubSummaryPRCreator,

    private val topicRepository: TopicRepository
) : ChatSummaryUsecase {
    override fun summarizeMessage(chats: List<Chat>): SummaryPR {
        val llmSummary = llmChatSummaryAgent.summarizeMessage(chats)

        topicRepository.addAll(llmSummary.topics.map { Topic(it) }.toSet())
        return prCreator.createPR(llmSummary)
    }
}