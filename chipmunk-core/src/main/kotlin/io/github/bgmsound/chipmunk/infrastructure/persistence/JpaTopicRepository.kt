package io.github.bgmsound.chipmunk.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository

internal interface JpaTopicRepository : JpaRepository<TopicEntity, String>