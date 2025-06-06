package io.github.bgmsound.chipmunk.infrastructure.persistence

import io.github.bgmsound.chipmunk.Topic
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "topics")
internal class TopicEntity(
    @Id
    @Column(name = "topic")
    val topic: String,
) {
    companion object {
        fun of(topic: Topic): TopicEntity {
            return TopicEntity(topic.name)
        }
    }

    fun toDomain(): Topic {
        return Topic(topic)
    }
}