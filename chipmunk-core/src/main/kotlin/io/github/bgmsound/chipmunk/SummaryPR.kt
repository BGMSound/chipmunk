package io.github.bgmsound.chipmunk

class SummaryPR(
    val id: SummaryId,
    val url: String
) {
    companion object {
        fun of(
            id: String,
            url: String
        ): SummaryPR {
            return SummaryPR(
                SummaryId.of(SummaryId.Platform.GITHUB, id),
                url
            )
        }
    }
}