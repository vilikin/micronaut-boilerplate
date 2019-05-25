package micronaut.server.http

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.reactivex.rxkotlin.toFlowable
import micronaut.server.Config
import micronaut.server.http.ErrorResponse
import org.reactivestreams.Publisher

@Filter("\${custom.server.base-path:}/**")
class SecurityFilter(
  val config: Config
) : HttpServerFilter {

  override fun doFilter(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> {
    if (request.headers["x-secret"] == config.server.secret) {
      return chain.proceed(request)
    }

    val errorResponse = HttpResponse.unauthorized<ErrorResponse>().body(
      ErrorResponse(
        status = 401,
        message = "Unauthorized",
        details = "Incorrect or missing x-secret header"
      )
    )

    return listOf(errorResponse)
      .toFlowable()
  }
}