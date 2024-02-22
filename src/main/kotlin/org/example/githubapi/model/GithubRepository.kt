package org.example.githubapi.model

data class GithubRepository(
    val name: String,
    val owner: GithubUser,
    val fork: Boolean
)
