//import BasicAuthAlgebra.Credentials
//
//
//trait Tupler[A, B] {
//  type Out
//}
//
//trait BasicAuthAlgebra extends Algebra {
//
//  import BasicAuthAlgebra._
//
//  type Protected[T] = Option[T]
//
//  //String in Right is basic auth realm
//  def toProtected[T](response: Response[Either[T, String]]): Response[Protected[T]]
//
//  implicit class BasicAuthEndpointOps[Req, Resp](endpoint: Endpoint[Req, Resp]) {
//    def withBasicAuth(implicit tupler: Tupler[Req, Credentials]): Endpoint[tupler.Out, Protected[Resp]] = {
//      val request = endpoint
//        .getRequest
//        .withHeaders(emptyReqHeaders.withHeader("Authorization").as[Credentials])
//      val response = endpoint.getResponse.or(
//        emptyResponse
//          .withHeaders(emptyRespHeaders.withHeader("WWW-Authenticate"))
//          .withStatusCode(401)
//      )
//
//      endpoint
//        .withRequest(request)
//        .withResponse(toProtected(response))
//    }
//  }
//
//}
//
//trait ValidatedRequestAlgebra extends Algebra {
//
//  type Validated[T] = Option[T]
//
//  def toValidated[T](response: Response[Either[T, Unit]]): Response[Validated[T]]
//
//  implicit class ValidatedResponseOps[T](response: Response[T]) {
//    def validated: Response[Validated[T]] = {
//      val r = response.or(emptyResponse.withStatusCode(400))
//      toValidated(r)
//    }
//  }
//
//}
//
//trait OptionalAlgebra extends Algebra {
//
//  type Optional[T] = Option[T]
//
//  def toOptional[T](response: Response[Either[T, Unit]]): Response[Optional[T]]
//
//  implicit class ValidatedResponseOps[T](response: Response[T]) {
//    def optional: Response[Optional[T]] = {
//      val r = response.or(emptyResponse.withStatusCode(404))
//      toOptional(r)
//    }
//  }
//
//}
//
//trait ExampleApi extends Algebra with ValidatedRequestAlgebra with BasicAuthAlgebra {
//
//
//  val parseFloat: Endpoint[(String, Credentials), Option[Option[String]]] = endpoint
//    .withRequest(
//      emptyRequest
//        .withMethod(Get)
//        .withStringBody
//        .withPath(root / "parseFloat"))
//    .withResponse(
//      emptyResponse
//        .withStringBody
//        .validated
//    )
//    .withBasicAuth
//}
//
//object BasicAuthAlgebra {
//
//  case class Credentials(username: String, password: String)
//
//}