package micronaut.server.http

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Maybe
import io.reactivex.Single
import micronaut.server.integrations.PlaceholderClient
import micronaut.server.integrations.PostWithComments

// Example controller to demonstrate reactive HttpClient usage
@Controller("\${custom.server.base-path:}/posts")
class PostController(
  private val placeholderClient: PlaceholderClient
) {
  @Get
  fun getPosts(): Single<List<PostWithComments>> {
    return placeholderClient.fetchPostsWithComments()
  }

  @Get("/{id}")
  fun getPost(id: Int): Maybe<PostWithComments> {
    return placeholderClient.fetchPostsWithComments()
      .flattenAsObservable { it }
      .filter { it.id == id }
      .firstElement()
  }
}
