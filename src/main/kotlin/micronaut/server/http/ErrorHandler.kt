package micronaut.server.http

import com.fasterxml.jackson.core.JsonParseException
import io.micronaut.core.convert.exceptions.ConversionErrorException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import micronaut.server.util.getLogger

class HttpError(val status: HttpStatus, override val message: String, cause: Throwable? = null) : Throwable(message, cause)

data class ErrorResponse(
  val details: String,
  val message: String,
  val status: Int
)

@Controller
class ErrorHandler {
  private val logger = getLogger<ErrorHandler>()

  @Error(global = true)
  fun onHttpError(req: HttpRequest<*>, error: HttpError): HttpResponse<ErrorResponse> {
    return HttpResponse.status<ErrorResponse>(error.status)
      .body(ErrorResponse(
        status = error.status.code,
        message = error.status.reason,
        details = error.message
      ))
  }

  @Error(global = true)
  fun onUnexpectedError(req: HttpRequest<*>, throwable: Throwable): HttpResponse<ErrorResponse> {
    return when (throwable) {
      // For invalid request body we want to give BAD_REQUEST with details
      is ConversionErrorException, is JsonParseException -> {
        val status = HttpStatus.BAD_REQUEST
        HttpResponse.status<ErrorResponse>(status)
          .body(ErrorResponse(
            status = status.code,
            message = status.reason,
            details = throwable.message!!
          ))
      }
      // We default to INTERNAL_SERVER_ERROR and leave out the internal details of the error
      else -> {
        logger.error("Unexpected error captured", throwable)

        val status = HttpStatus.INTERNAL_SERVER_ERROR
        HttpResponse.status<ErrorResponse>(status)
          .body(ErrorResponse(
            status = status.code,
            message = status.reason,
            details = "More details can be found in the application logs. Contact developers."
          ))
      }
    }
  }
}
