package org.example.githubapi.model

data class RepositoryDTO(
    val repositoryName: String,
    val ownerLogin: String,
    val branches: List<BranchDTO>
)
