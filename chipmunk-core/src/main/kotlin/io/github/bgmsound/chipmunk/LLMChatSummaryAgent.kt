package io.github.bgmsound.chipmunk

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value

@Component
class LLMChatSummaryAgent(
    @Value("\${base-prompt}") private val basePrompt: String,

    private val objectMapper: ObjectMapper,
    private val model: ChatModel
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val systemPrompt = """
            응답 형식은 다음과 같아.
            ${objectMapper.writeValueAsString(SAMPLE_SUMMARY)}
            
            JSON 형식으로 응답을 반환해줘.
            - 모든 문자열 값 안에 줄바꿈(엔터)이 있으면 반드시 \n으로 이스케이프해서 반환해.  
            - 실제 줄바꿈(엔터)이 들어가면 안 되고, 무조건 \n으로 처리해야 해.  
            - JSON key와 value는 반드시 쌍따옴표(")로 감싸서 올바른 JSON 포맷으로 반환해줘.  
            - 반환 결과에 불필요한 이스케이프(\")나 역슬래시가 남지 않게 해줘.
            
            기존에 존재하는 토픽 리스트:
            ${objectMapper.writeValueAsString(listOf<String>())}
            
            기존에 존재하는 토픽 중 대화 주제에 관련한 토픽이 있으면 포함해주고,
            원하는 토픽이 존재하지 않거나 대화 주제에 관련한 새로운 토픽이 있다면, topics 리스트에 추가해서 응답해줘.
            
            토픽 선정 예시
            - 토픽은 한두 단어(최대 세 단어)로 간결하게 작성해줘.
            - 토픽은 문장형이 아니라 명사 위주(카테고리, 키워드, 개념어 등)로 만들어줘.
            - 불필요하게 길거나 구체적이지 않은 토픽은 피하고, 핵심 개념만 뽑아줘.
        """.trimIndent()

    fun summarizeMessage(chats: List<Chat>): LLMSummary {
        val conversation = StringBuilder().apply {
            chats.forEach { chat ->
                append("- **${chat.authorName}**: ${chat.content}\n")
            }
        }
        val prompt = basePrompt
            .replace("{conversation}", conversation.toString())
            .replace("{system-prompt}", systemPrompt)
        val summaryRawContent = model.call(prompt)
            .replace(Regex("^```json\\s*"), "")
            .replace(Regex("\\s*```$"), "")
            .trim()
        logger.info(summaryRawContent)
        return objectMapper.readValue(summaryRawContent, LLMSummary::class.java)
    }

    private companion object {
        private val SAMPLE_SUMMARY = LLMSummary(
            title = "대화 요약의 타이틀",
            topics = listOf("대화 관련 주제1", "대화 관련 주제2"),
            content = "대화 요약 내용 (마크다운 형식)",
        )
    }
}