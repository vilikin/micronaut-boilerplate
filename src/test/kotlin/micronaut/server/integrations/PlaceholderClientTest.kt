package micronaut.server.integrations

import io.kotlintest.matchers.collections.containExactly
import io.kotlintest.should
import io.micronaut.test.annotation.MicronautTest
import micronaut.server.test.utils.MockServerTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockserver.matchers.Times
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import java.io.File
import javax.inject.Inject

@DisplayName("Placeholder client")
@MicronautTest
class PlaceholderClientTest : MockServerTest() {

  @Inject
  lateinit var placeholderClient: PlaceholderClient

  @Test
  fun `should be able to query posts`() {
    mockPostsEndpoint()

    val expectedPosts = listOf(
      Post(
        id = 1,
        title = "Test",
        body = "Test"
      )
    )

    val posts = placeholderClient.fetchPosts().blockingGet()
    posts should containExactly(expectedPosts)
  }

  @Test
  fun `should be able to query comments`() {
    mockCommentsEndpoint()

    val expectedComments = listOf(
      Comment(
        id = 1,
        postId = 1,
        name = "Test",
        body = "Test"
      )
    )

    val comments = placeholderClient.fetchComments().blockingGet()
    comments should containExactly(expectedComments)
  }

  @Test
  fun `should be able to combine posts with comments`() {
    mockPostsEndpoint()
    mockCommentsEndpoint()

    val expectedPosts = listOf(
      PostWithComments(
        id = 1,
        title = "Test",
        body = "Test",
        comments = listOf(
          Comment(
            id = 1,
            postId = 1,
            name = "Test",
            body = "Test"
          )
        )
      )
    )

    val posts = placeholderClient.fetchPostsWithComments().blockingGet()
    posts should containExactly(expectedPosts)
  }

  private fun mockPostsEndpoint() {
    val mockResponse = File("src/test/resources/posts.json").readText()

    mockServer
      .`when`(
        HttpRequest()
          .withPath("/posts")
          .withMethod("GET"),
        Times.once()
      ).respond(
        HttpResponse()
          .withStatusCode(200)
          .withHeader(Header("content-type", "application/json"))
          .withBody(mockResponse)
      )
  }

  private fun mockCommentsEndpoint() {
    val mockResponse = File("src/test/resources/comments.json").readText()

    mockServer
      .`when`(
        HttpRequest()
          .withPath("/comments")
          .withMethod("GET"),
        Times.once()
      ).respond(
        HttpResponse()
          .withStatusCode(200)
          .withHeader(Header("content-type", "application/json"))
          .withBody(mockResponse)
      )
  }
}