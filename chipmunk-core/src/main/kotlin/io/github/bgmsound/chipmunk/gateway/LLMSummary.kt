package io.github.bgmsound.chipmunk.gateway

data class LLMSummary(
    /**
     * LLM 요약 내용 (임시)
     */
    val topics: List<String> = listOf("사용자 인증 방식 논의"),
    val title: String = "사용자 인증 방식 논의 요약",
    val content: String = """
        ### 사용자 인증 방식 논의 요약

        - **JWT vs 세션 기반 인증**
          - SPA(싱글 페이지 앱)에서는 JWT가 확장성/유지보수 측면에서 더 적합
          - JWT 사용 시 토큰 관리 및 만료 처리에 신경 써야 함

        - **Refresh Token 보안 관리**
          - Refresh Token은 HttpOnly 쿠키에 저장하는 것이 가장 안전
          - 보안 위협(탈취 등)이 크면 서버에서 블랙리스트(토큰 무효화) 기능을 추가

        - **실제 설계**
          - JWT + HttpOnly 쿠키 조합으로 설계 권장

        - **추가 자료**
          - JWT 인증 샘플 코드는 슬랙을 통해 별도 공유

        ---
        **참여자:**  
        - A개발자: 인증 방식 및 보안 관련 질문  
        - B개발자: 경험 공유 및 실질적 설계/보안 조언 제공
    """.trimIndent()
)