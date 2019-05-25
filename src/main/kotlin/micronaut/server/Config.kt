package micronaut.server

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("custom")
class Config(
  val server: ServerConfig
) {
  lateinit var example: String
}

@ConfigurationProperties("custom.server")
class ServerConfig {
  lateinit var secret: String
}