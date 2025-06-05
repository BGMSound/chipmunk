package io.github.bgmsound.chipmunk.usecase

import io.github.bgmsound.chipmunk.Chat
import io.github.bgmsound.chipmunk.SummaryResult

interface ChatSummaryUsecase {

    fun summarizeMessage(chats: List<Chat>): SummaryResult

}