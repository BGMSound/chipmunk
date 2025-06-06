package io.github.bgmsound.chipmunk.infrastructure.persistence

import io.github.bgmsound.chipmunk.Topic
import io.github.bgmsound.chipmunk.TopicRepository
import org.springframework.stereotype.Repository

@Repository
internal class TopicRepositoryJpaDelegate(
    private val jpaTopicRepository: JpaTopicRepository
) : TopicRepository {
    private val topics: MutableSet<Topic> = mutableSetOf()

    init {
        jpaTopicRepository.findAll().forEach { entity ->
            topics.add(entity.toDomain())
        }
    }

    override fun getAll(): Set<Topic> {
        return topics.toSet()
    }

    override fun addAll(topics: Set<Topic>) {
        val newTopics = topics.filterNot { this.topics.contains(it) }
        if (newTopics.isNotEmpty()) {
            jpaTopicRepository.saveAll(newTopics.map { TopicEntity.of(it) })
            this.topics.addAll(newTopics)
        }
    }
}