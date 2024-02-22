import org.example.githubapi.controller.GithubController
import org.example.githubapi.model.GithubRepository
import org.example.githubapi.model.GithubUser
import org.example.githubapi.service.GithubService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

@ExtendWith(MockitoExtension::class)
class GithubControllerTest {

    @Mock
    private lateinit var githubService: GithubService

    @InjectMocks
    private lateinit var githubController: GithubController

    @Test
    fun `test listRepositories with existing repositories`() {
        // Given
        val username = "testUser"
        val repositories = arrayOf(
            GithubRepository("repo1", owner = GithubUser("login1"), fork = false),
            GithubRepository("repo2", owner = GithubUser("login1"), fork = false)
        )

        `when`(githubService.getRepositories(username)).thenReturn(repositories)
        `when`(githubService.getBranches(username, "repo1")).thenReturn(arrayOf())
        `when`(githubService.getBranches(username, "repo2")).thenReturn(arrayOf())

        // When
        val response = githubController.listRepositories(username)

        // Then
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body is List<*>)
        assert((response.body as List<*>).size == 2)
    }

    @Test
    fun `test listRepositories with non-existing repositories`() {
        // Given
        val username = "testUser"

        `when`(githubService.getRepositories(username)).thenReturn(emptyArray())

        // When
        val response = githubController.listRepositories(username)

        // Then
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body is List<*>)
        assert((response.body as List<*>).isEmpty())
    }

    @Test
    fun `test listRepositories with not found exception`() {
        // Given
        val username = "testUser"
        val notFoundException = HttpClientErrorException(HttpStatus.NOT_FOUND)

        `when`(githubService.getRepositories(username)).thenThrow(notFoundException)

        // When
        val response = githubController.listRepositories(username)

        // Then
        assert(response.statusCode == HttpStatus.NOT_FOUND)
        assert(response.body is Map<*, *>)
        assert((response.body as Map<*, *>)["message"] == "User not found")
    }

    @Test
    fun `test listRepositories with rate limit exceeded exception`() {
        // Given
        val username = "testUser"
        val rateLimitExceededException = HttpClientErrorException(HttpStatus.FORBIDDEN)

        `when`(githubService.getRepositories(username)).thenThrow(rateLimitExceededException)

        // When
        val response = githubController.listRepositories(username)

        // Then
        assert(response.statusCode == HttpStatus.TOO_MANY_REQUESTS)
        assert(response.body is Map<*, *>)
        assert((response.body as Map<*, *>)["message"] == "API rate limit exceeded")
    }
}
