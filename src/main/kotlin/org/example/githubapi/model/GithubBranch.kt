package org.example.githubapi.model

data class GithubBranch(
    val name: String,
    val commit: GithubCommit
)
