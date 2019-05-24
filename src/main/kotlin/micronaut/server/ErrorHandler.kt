package micronaut.server

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error

class HttpError(val status: HttpStatus, override val message: String, cause: Throwable) : Throwable(message, cause)

data class ErrorResponse(
  val details: String,
  val message: String,
  val status: Int
)

@Controller
class ErrorController {
  @Error(global = true)
  fun onHttpError(req: HttpRequest<*>, error: HttpError): HttpResponse<ErrorResponse> {
    return HttpResponse.status<ErrorResponse>(error.status)
      .body(ErrorResponse(
        status = error.status.code,
        message = error.status.reason,
        details = error.message
      ))
  }
}
