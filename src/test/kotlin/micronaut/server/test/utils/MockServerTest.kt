package micronaut.server.test.utils

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.TestInstance
import org.mockserver.client.MockServerClient
import org.mockserver.integration.ClientAndServer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class MockServerTest {
  val mockServer: MockServerClient = ClientAndServer.startClientAndServer(9090)

  @AfterAll
  fun tearDown() {
    mockServer.close()
  }
}