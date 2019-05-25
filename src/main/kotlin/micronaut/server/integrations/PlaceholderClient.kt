package micronaut.server.integrations

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.reactivex.Single
import io.reactivex.rxkotlin.cast
import io.reactivex.rxkotlin.zipWith
import micronaut.server.http.HttpError
import micronaut.server.util.getLogger
import javax.inject.Singleton

data class Post(
  val id: Int,
  val title: String,
  val body: String
)

data class Comment(
  val id: Int,
  val postId: Int,
  val name: String,
  val body: String
)

data class PostWithComments(
  val id: Int,
  val title: String,
  val body: String,
  val comments: List<Comment>
)

@Singleton
class PlaceholderClient(
  @param:Client("\${custom.integrations.placeholder.url}")
  private val httpClient: RxHttpClient
) {
  private val logger = getLogger<PlaceholderClient>()

  fun fetchPostsWithComments(): Single<List<PostWithComments>> {
    val postsSingle = fetchPosts()
    val commentsSingle = fetchComments()

    logger.info("I started fetching stuff!")

    return postsSingle.zipWith(commentsSingle) { posts, comments ->
      posts.map { post ->
        val commentsOfPost = comments.filter { it.postId == post.id }
        PostWithComments(post.id, post.title, post.body, commentsOfPost)
      }
    }
  }

  fun fetchPosts(): Single<List<Post>> {
    val req = HttpRequest.GET<Any>("/posts")
    return httpClient
      .retrieve(req, Argument.of(List::class.java, Post::class.java))
      .firstElement()
      .toSingle()
      .onErrorResumeNext { e: Throwable ->
        logger.error("Failed to fetch posts", e)
        Single.error(HttpError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching posts", e))
      }
      .cast()
  }

  fun fetchComments(): Single<List<Comment>> {
    val req = HttpRequest.GET<Any>("/comments")
    return httpClient
      .retrieve(req, Argument.of(List::class.java, Comment::class.java))
      .firstElement()
      .toSingle()
      .onErrorResumeNext { e: Throwable ->
        logger.error("Failed to fetch comments", e)
        Single.error(HttpError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching comments", e))
      }
      .cast()
  }
}
