package micronaut.server.http

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Single
import micronaut.server.core.Person
import micronaut.server.core.PersonService

// Example controller to demonstrate reactive DB access
@Controller("\${custom.server.base-path:}/persons")
class PersonController(
  private val personService: PersonService
) {
  @Get
  fun getPersons(): Single<List<Person>> {
    return personService.findAllPersons()
      .toList()
  }
}
