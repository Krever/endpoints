package endpoints.newalgebra.basic

import endpoints.Tupler

import scala.language.higherKinds

/**
  * Algebra interface for describing endpoints made of requests and responses.
  *
  * Requests and responses contain headers and entity.
  *
  * {{{
  *   /**
  *     * Describes an HTTP endpoint whose:
  *     *  - request uses verb “GET”,
  *     *  - URL is made of path “/foo”,
  *     *  - response has no entity
  *     */
  *   val example = endpoint(get(path / "foo"), emptyResponse)
  * }}}
  */
trait Endpoints extends Urls with Methods {

  /** Information carried by requests’ headers */
  type RequestHeaders[A]

  /**
    * No particular information. Does not mean that the headers *have to*
    * be empty. Just that, from a server point of view no information will
    * be extracted from them, and from a client point of view no particular
    * headers will be built in the request.
    */
  def emptyHeaders: RequestHeaders[Unit]


  /** Information carried by a whole request (headers and entity) */
  type Request[A]

  /** Information carried by request entity */
  type RequestEntity[A]

  /**
    * Empty request.
    */
  def emptyRequest: RequestEntity[Unit]


  /**
    * Request for given parameters
    *
    * @param method Request method
    * @param url Request URL
    * @param entity Request entity
    * @param headers Request headers
    */
  def request[A, B, C, AB](
    method: Method,
    url: Url[A],
    entity: RequestEntity[B] = emptyRequest,
    headers: RequestHeaders[C] = emptyHeaders
  )(implicit tuplerAB: Tupler.Aux[A, B, AB], tuplerABC: Tupler[AB, C]): Request[tuplerABC.Out]

  /**
    * Helper method to perform GET request
    */
  final def get[A, B](
    url: Url[A],
    headers: RequestHeaders[B] = emptyHeaders
  )(implicit tuplerAC: Tupler[A, B]): Request[tuplerAC.Out] = request(Get, url, headers = headers)

  /**
    * Helper method to perform POST request
    */
  final def post[A, B, C, AB](
    url: Url[A],
    entity: RequestEntity[B],
    headers: RequestHeaders[C] = emptyHeaders
  )(implicit tuplerAB: Tupler.Aux[A, B, AB], tuplerABC: Tupler[AB, C]): Request[tuplerABC.Out] = request(Post, url, entity, headers)


  /** Information carried by a response */
  type Response[A]

  /**
    * Empty response.
    */
  def emptyResponse: Response[Unit]

  /**
    * Information carried by an HTTP endpoint
    * @tparam A Information carried by the request
    * @tparam B Information carried by the response
    */
  type Endpoint[A, B]

  /**
    * HTTP endpoint.
    *
    * @param request Request
    * @param response Response
    */
  def endpoint[A, B](request: Request[A], response: Response[B]): Endpoint[A, B]

  /**
    * Information carried by a multiplexed HTTP endpoint.
    */
  type MuxEndpoint[Req <: MuxRequest, Resp, Transport]

  /**
    * Multiplexed HTTP endpoint.
    *
    * A multiplexing endpoint makes it possible to use several request
    * and response types in the same HTTP endpoint. In other words, it
    * allows to define several different actions through a singe HTTP
    * endpoint.
    *
    * @param request The request
    * @param response The response
    * @tparam Req The base type of possible requests
    * @tparam Resp The base type of possible responses
    * @tparam Transport The data type used to transport the requests and responses
    */
  def muxEndpoint[Req <: MuxRequest, Resp, Transport](
    request: Request[Transport],
    response: Response[Transport]
  ): MuxEndpoint[Req, Resp, Transport]

}

/**
  * Multiplexed request type
  */
trait MuxRequest {
  type Response
}
