package io.github.bgmsound.chipmunk.usecase

import io.github.bgmsound.chipmunk.Chat
import io.github.bgmsound.chipmunk.SummaryPR
import io.github.bgmsound.chipmunk.GithubSummaryPRCreator
import io.github.bgmsound.chipmunk.LLMChatSummaryAgent
import io.github.bgmsound.chipmunk.Topic
import io.github.bgmsound.chipmunk.TopicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
class ChatSummaryUsecase(
    private val llmChatSummaryAgent: LLMChatSummaryAgent,
    private val prCreator: GithubSummaryPRCreator,

    private val topicRepository: TopicRepository,
    private val coroutineScope: CoroutineScope
) {
    suspend fun summarizeMessage(chats: List<Chat>): SummaryPR {
        val llmSummary = llmChatSummaryAgent.summarizeMessage(chats)

        coroutineScope.launch {
            topicRepository.addAll(llmSummary.topics.map { Topic(it) }.toSet())
        }
        return prCreator.createPR(llmSummary)
    }
}