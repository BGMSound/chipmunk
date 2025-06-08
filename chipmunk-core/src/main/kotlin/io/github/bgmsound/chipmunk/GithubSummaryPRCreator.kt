package io.github.bgmsound.chipmunk

import org.kohsuke.github.*
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class GithubSummaryPRCreator(
    private val remoteRepository: GHRepository
) {
    fun createPR(summary: LLMSummary): SummaryPR {
        val date = LocalDate.now().toString()

        val prBranch = "${date}-${createRandomTail()}"
        val baseBranch = remoteRepository.defaultBranch

        val baseRef = remoteRepository.getEnsuredRef(baseBranch)
        remoteRepository.createRef("refs/heads/$prBranch", baseRef.`object`.sha)

        commitDailySummary(prBranch, summary)
        for (topic in summary.topics) {
            commitTopicSummary(summary.title, topic, prBranch)
        }
        val prTitle = "${summary.title} - (${date})"
        val prBody = "${summary.content}\n\n\n\n**[자동생성 요약 PR 정보]**\n- 날짜: $date\n- 토픽: ${summary.topics}"
        val pullRequest = remoteRepository.createPullRequest(prTitle, prBranch, baseBranch, prBody)

        return SummaryPR.of(
            pullRequest.id.toString(),
            pullRequest.title,
            pullRequest.htmlUrl.toString()
        )
    }

    private fun commitDailySummary(branch: String, summary: LLMSummary): GHContentUpdateResponse {
        val dailyPath = "docs/daily/$branch.md"
        return remoteRepository.createContent()
            .branch(branch)
            .path(dailyPath)
            .content(
                StringBuilder().apply {
                    append("${summary.content}\n\n")
                    append("\n\n**[자동생성 요약 PR 정보]**\n- 날짜: ${LocalDate.now()}\n- 토픽: ${summary.topics.joinToString(", ")}\n")
                }.toString()
            )
            .message("Add daily summary for topic of ${summary.topics}")
            .commit()
    }

    private fun commitTopicSummary(title: String, topic: String, branch: String): GHContentUpdateResponse {
        val topicPath = "docs/topics/${topic.replace(" ", "-")}.md"
        val commitMessage = "Add topic summary for $topic on ${LocalDate.now()}}"

        val file = try {
            remoteRepository.getFileContent(topicPath, branch)
        } catch (exception: GHFileNotFoundException) {
            null
        }
        val content = StringBuilder().apply {
            append(file?.let {
                val origin = file.read().bufferedReader().readText()
                "$origin\n\n"
            } ?: "## \"$topic\" 주제와 관련된 대화 목록\n")
            append("- [${title} - (${branch})](../daily/${branch}.md)")
        }.toString()
        return remoteRepository.createContent()
            .branch(branch)
            .path(topicPath)
            .content(content)
            .message(commitMessage)
            .shaIfExists(file)
            .commit()
    }

    private fun GHContentBuilder.shaIfExists(file: GHContent?): GHContentBuilder {
        return if (file != null) {
            this.sha(file.sha)
        } else {
            this
        }
    }

    private fun GHRepository.getEnsuredRef(branch: String): GHRef {
        return try {
            getRef("heads/$branch")
        } catch (exception: GHFileNotFoundException) {
            createContent()
                .branch(branch)
                .path("README.md")
                .content("README.md")
                .message("Initialize repository with README.md")
                .commit()
            getRef("heads/$branch")
        }
    }

    private fun createRandomTail(): String {
        return (1..8)
            .map { ('a'..'z').random() }
            .joinToString("")
    }
}