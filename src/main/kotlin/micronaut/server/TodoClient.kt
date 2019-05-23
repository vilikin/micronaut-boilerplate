package micronaut.server

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.reactivex.Maybe
import javax.inject.Singleton

data class Todo(
  val id: String,
  val title: String
)

@Singleton
class TodoClient(
  @param:Client("https://jsonplaceholder.typicode.com")
  private val httpClient: RxHttpClient
) {

  internal fun fetchTodos(): Maybe<List<Todo>> {
    val uri = "/todos"
    val req = HttpRequest.GET<Any>(uri)
    val flowable = httpClient.retrieve(req, Argument.of(List::class.java, Todo::class.java))
    return flowable.firstElement() as Maybe<List<Todo>>
  }
}
