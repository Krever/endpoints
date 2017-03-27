package endpoints.newalgebra.basic

import endpoints.Tupler


trait Responses {

  type Response[T]
  type ResponseBody[T]
  type ResponseHeader[T]
  type ResponseHeaders[T]


  def emptyResponse: Response[Unit]
  def setBody[Req, B](request: Response[Req], path: ResponseBody[B])(implicit tupler: Tupler[Req, B]): Response[tupler.Out]
  def setHeaders[Req, H](request: Response[Req], headers: ResponseHeaders[H])(implicit tupler: Tupler[Req, H]): Response[tupler.Out]


  // TODO support Content-Type
  def emptyResponseBody: ResponseBody[Unit]
  def stringResponseBody: ResponseBody[String]


  def emptyReqHeaders: ResponseHeaders[Unit]
  def addHeader[T](headers: ResponseHeaders[T], headerName: String)(implicit tupler: Tupler[T, String]): Response[tupler.Out]
  def mapHeader[T, U](headers: ResponseHeaders[T])(implicit header: ResponseHeader[U]): ResponseHeaders[U]



  implicit class ResponseOps[T](response: Response[T]) {
    def withHeaders[U](headers: ResponseHeaders[U])(implicit tupler: Tupler[T, U]): Response[tupler.Out] = ???

    def withStringBody(implicit tupler: Tupler[T, String]): Response[tupler.Out] = withBody(stringRespBody)(tupler)

    def withBody[U](body: ResponseBody[U])(implicit tupler: Tupler[T, U]): Response[tupler.Out] = ???

    def or[U](response2: Response[U]): Response[Either[T, U]] = ???

    def withStatusCode(code: Int): Response[T] = ???
  }

  implicit class RespHeadersOps[T](headers: ResponseHeaders[T]) {
    def withHeader[U](headerName: String)(implicit tupler: Tupler[T, String]): ResponseHeaders[tupler.Out] = ???

    def as[U]: ResponseHeaders[U] = ???
  }


}
