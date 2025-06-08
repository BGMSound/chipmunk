package io.github.bgmsound.chipmunk

class SummaryId(
    val platform: Platform,
    val value: String
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