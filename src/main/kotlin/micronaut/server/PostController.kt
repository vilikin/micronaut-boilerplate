package micronaut.server

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Maybe
import io.reactivex.Single

@Controller("/posts")
class PostController(
  private val postClient: PostClient
) {
  @Get
  fun getPosts(): Single<List<PostWithComments>> {
    return postClient.fetchPostsWithComments()
  }

  @Get("/{id}")
  fun getPost(id: Int): Maybe<PostWithComments> {
    return postClient.fetchPostsWithComments()
      .flattenAsObservable { it }
      .filter { it.id == id }
      .firstElement()
  }
}