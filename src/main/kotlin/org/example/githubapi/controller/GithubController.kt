package org.example.githubapi.controller

import org.example.githubapi.model.BranchDTO
import org.example.githubapi.model.RepositoryDTO
import org.example.githubapi.service.GithubService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException

@RestController
@RequestMapping("/api/github")
class GithubController(private val service: GithubService) {

    @GetMapping("/repositories/{username}")
    fun listRepositories(
        @PathVariable username: String
    ): ResponseEntity<Any> {
        try {
            val repositories = service.getRepositories(username).filter { !it.fork }

            if (repositories.isNotEmpty()) {
                val response = repositories.map { repo ->
                    val branches = service.getBranches(username, repo.name)
                        .map { branch ->
                            BranchDTO(branch.name, branch.commit.sha)
                        }
                    RepositoryDTO(repo.name, repo.owner.login, branches)
                }

                return ResponseEntity.ok()
                    .body(response)
            }

            return ResponseEntity.ok()
                .body(emptyList<RepositoryDTO>())

        } catch (ex: HttpClientErrorException) {
            return when (ex.statusCode) {
                HttpStatus.NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mapOf("status" to HttpStatus.NOT_FOUND.value(), "message" to "User not found"))

                HttpStatus.FORBIDDEN -> ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(
                        mapOf(
                            "status" to HttpStatus.TOO_MANY_REQUESTS.value(),
                            "message" to "API rate limit exceeded"
                        )
                    )

                else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                        mapOf(
                            "status" to HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "message" to "An unexpected error occurred"
                        )
                    )
            }
        }
    }
}