package io.github.bgmsound.chipmunk

import java.time.LocalDateTime


data class Chat(
    val id: String,
    val authorName: String,
    val content: String,
    val timestamp: LocalDateTime
)