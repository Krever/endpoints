package endpoints.algebra.client

import com.github.tomakehurst.wiremock.client.WireMock._
import endpoints.algebra.{BasicAuthTestApi, BasicAuthentication}

trait BasicAuthTestSuite extends ClientTestBase {

  val api: BasicAuthTestApi
  override type Endpoint[Req, Resp] = api.basicAuth.endpoints.Endpoint[Req, Resp]

  def basicAuthSuite() = {

    "Client interpreter" should {

      "authenticate with given credentials" in {

        val credentials = BasicAuthentication.Credentials("user1", "pass2")
        val response = "wiremockeResponse"

        wireMockServer.stubFor(get(urlEqualTo("/users"))
          .withBasicAuth(credentials.username, credentials.password)
          .willReturn(aResponse()
            .withStatus(200)
            .withBody(response)))

        whenReady(call(api.protectedEndpoint, credentials))(_ shouldEqual Some(response))

      }

      "return None if authentication failed" in {

        val credentials = BasicAuthentication.Credentials("user1", "pass2")

        wireMockServer.stubFor(get(urlEqualTo("/users"))
          .willReturn(aResponse()
            .withStatus(403)
            .withBody("")))

        whenReady(call(api.protectedEndpoint, credentials))(_ shouldEqual None)

      }
    }
  }
}