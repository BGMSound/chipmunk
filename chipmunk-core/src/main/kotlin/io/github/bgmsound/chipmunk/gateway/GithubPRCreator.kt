package io.github.bgmsound.chipmunk.gateway

import org.kohsuke.github.*
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class GithubPRCreator(
    private val remoteRepository: GHRepository,
) {
    fun createPR(summary: LLMSummary) {
        val date = LocalDate.now().toString()

        val prBranch = "${date}-${createRandomTail()}"
        val baseBranch = remoteRepository.defaultBranch

        val baseRef = getEnsuredRef(baseBranch)
        remoteRepository.createRef("refs/heads/$prBranch", baseRef.`object`.sha)

        commitDailySummary(prBranch, summary)
        for (topic in summary.topics) {
            commitTopicSummary(summary.title, topic, prBranch)
        }
        val prTitle = "${summary.title} -(${date})"
        val prBody = "자동생성 요약 PR\n- 날짜: $date\n- 토픽: ${summary.topics}"
        remoteRepository.createPullRequest(prTitle, prBranch, baseBranch, prBody)
    }

    private fun getEnsuredRef(branch: String): GHRef {
        return try {
            remoteRepository.getRef("heads/$branch")
        } catch (exception: GHFileNotFoundException) {
            remoteRepository.createContent()
                .branch(branch)
                .path("README.md")
                .content("README.md")
                .message("Initialize repository with README.md")
                .commit()
            remoteRepository.getRef("heads/$branch")
        }
    }


    private fun commitDailySummary(branch: String, summary: LLMSummary): GHContentUpdateResponse {
        val dailyPath = "docs/daily/$branch.md"
        return remoteRepository.createContent()
            .branch(branch)
            .path(dailyPath)
            .content(summary.content)
            .message("Add daily summary for topic of ${summary.topics}")
            .commit()
    }

    private fun commitTopicSummary(title: String, topic: String, branch: String): GHContentUpdateResponse {
        val topicPath = "docs/topics/${topic}.md"
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
            } ?: "## $topic 와 관련된 대화 목록\n")
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

    private fun createRandomTail(): String {
        return (1..8)
            .map { ('a'..'z').random() }
            .joinToString("")
    }
}