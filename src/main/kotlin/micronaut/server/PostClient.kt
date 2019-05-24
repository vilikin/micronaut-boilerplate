package micronaut.server

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.reactivex.Single
import io.reactivex.rxkotlin.cast
import io.reactivex.rxkotlin.zipWith
import javax.inject.Singleton

open class Post(
  val id: Int,
  val title: String,
  val body: String
)

class Comment(
  val id: Int,
  val postId: Int,
  val name: String,
  val body: String
)

class PostWithComments(
  post: Post,
  val comments: List<Comment>
) : Post(post.id, post.title, post.body)

@Singleton
class PostClient(
  @param:Client("https://jsonplaceholder.typicode.com")
  private val httpClient: RxHttpClient
) {
  fun fetchPostsWithComments(): Single<List<PostWithComments>> {
    val postsSingle = fetchPosts()
    val commentsSingle = fetchComments()

    return postsSingle.zipWith(commentsSingle) { posts, comments ->
      posts.map { post ->
        val commentsOfPost = comments.filter { it.postId == post.id }
        PostWithComments(post, commentsOfPost)
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
        println("Error $e")
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
        println("Error $e")
        Single.error(HttpError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching comments", e))
      }
      .cast()
  }
}
