package io.github.bgmsound.chipmunk

class SummaryId(
    val platform: Platform,
    val id: String
) {
    enum class Platform {
        GITHUB
    }

    companion object {
        fun of(
            platform: Platform,
            id: String
        ): SummaryId {
            return SummaryId(platform, id)
        }
    }
}