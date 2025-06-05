package io.github.bgmsound.chipmunk

import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GithubRepositoryConfiguration(
    @Value("\${app.github.token}") private val token: String,
    @Value("\${app.github.repository-name}") private val repositoryName: String
) {
    @Bean
    fun github(): GitHub = GitHubBuilder().withOAuthToken(token).build()

    @Bean
    fun githubRepository(github: GitHub): GHRepository = github.getRepository(repositoryName)

}