package org.example.githubapi.service

import org.example.githubapi.model.GithubBranch
import org.example.githubapi.model.GithubRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GithubService {
    private val restTemplate = RestTemplate()

    fun getRepositories(username: String): Array<GithubRepository> {
        val url = "https://api.github.com/users/$username/repos"

        return restTemplate.getForObject(url, Array<GithubRepository>::class.java) ?: emptyArray()
    }

    fun getBranches(username: String, repositoryName: String): Array<GithubBranch> {
        val url = "https://api.github.com/repos/$username/$repositoryName/branches"

        return restTemplate.getForObject(url, Array<GithubBranch>::class.java) ?: emptyArray()
    }
}