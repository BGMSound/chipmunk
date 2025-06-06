package io.github.bgmsound.chipmunk

interface TopicRepository {

    fun getAll(): Set<Topic>

    fun addAll(topics: Set<Topic>)

}