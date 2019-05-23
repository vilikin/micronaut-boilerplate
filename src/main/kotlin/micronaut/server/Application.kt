package micronaut.server

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License

@OpenAPIDefinition(
  info = Info(
    title = "Hello World",
    version = "0.0",
    description = "My API",
    license = License(name = "Apache 2.0", url = "http://foo.bar"),
    contact = Contact(url = "http://vilik.in", name = "Vili", email = "vili.kinnunen1@gmail.com")
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