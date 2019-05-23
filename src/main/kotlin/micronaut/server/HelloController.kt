package micronaut.server

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Attributes of a person")
data class Person(
  @field:Schema(title = "Name of a person")
  val name: String,
  @field:Schema(title = "Age of a person", nullable = true)
  val age: Int?
)

@Controller("/")
class HelloController(
  private val todoClient: TodoClient
) {
  @Post("/hello")
  fun hello(@Body body: Person): Person {
    return body
  }

  @Get("/todos")
  fun todos(): List<Todo> {
    val todos = todoClient.fetchTodos().blockingGet()
    return todos
  }
}
