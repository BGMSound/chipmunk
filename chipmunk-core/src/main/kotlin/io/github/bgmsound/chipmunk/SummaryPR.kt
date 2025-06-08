package io.github.bgmsound.chipmunk

class SummaryPR(
    val id: SummaryId,
    val title: String,
    val url: String
) {
    companion object {
        fun of(
            id: String,
            title: String,
            url: String
        ): SummaryPR {
            return SummaryPR(
                SummaryId.of(SummaryId.Platform.GITHUB, id),
                title,
                url
            )
        }
    }
}