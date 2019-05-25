package micronaut.server

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
  info = Info(
    title = "Micronaut Boilerplate",
    version = "1.0",
    description = "Micronaut Boilerplate API"
  )
)
object Application {

  @JvmStatic
  fun main(args: Array<String>) {
    Micronaut.build()
      .packages("micronaut.server")
      .mainClass(Application.javaClass)
      .start()
  }
}
