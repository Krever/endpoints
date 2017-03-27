package endpoints.newalgebra.basic

import endpoints.Tupler

/**
  * Created by wpitula on 3/27/17.
  */
trait Requests extends Urls with Methods {

  type Request[T]
  type RequestHeaders[T]
  type RequestHeader[T]
  type RequestBody[T]


  def emptyRequest: Request[Unit]
  def setMethod[Req](request: Request[Req], method: Method): Request[Req]
  def setUrl[Req, U](request: Request[Req], path: Url[U])(implicit tupler: Tupler[Req, U]): Request[tupler.Out]
  def setBody[Req, B](request: Request[Req], path: RequestBody[B])(implicit tupler: Tupler[Req, B]): Request[tupler.Out]
  def setHeaders[Req, H](request: Request[Req], headers: RequestHeaders[H])(implicit tupler: Tupler[Req, H]): Request[tupler.Out]


  // TODO support Content-Type
  def emptyRequestBody: RequestBody[Unit]
  def stringRequestBody: RequestBody[String]


  def emptyReqHeaders: RequestHeaders[Unit]
  def addHeader[T](headers: RequestHeaders[T], headerName: String)(implicit tupler: Tupler[T, String]): Request[tupler.Out]
  def mapHeader[T, U](headers: RequestHeaders[T])(implicit header: RequestHeader[U]): RequestHeaders[U]


  implicit class RequestOps[T](request: Request[T]) {
    def withHeaders[U](headers: RequestHeaders[U])(implicit tupler: Tupler[T, U]): Request[tupler.Out] = setHeaders(request, headers)

    def withUrl[U](url: Url[U])(implicit tupler: Tupler[T, U]): Request[tupler.Out] = setUrl(request, url)

    def withBody[U](body: RequestBody[U])(implicit tupler: Tupler[T, U]): Request[tupler.Out] = setBody(request, body)

    def withStringBody(implicit tupler: Tupler[T, String]): Request[tupler.Out] = withBody(stringRequestBody)

    def withMethod(method: Method): Request[T] = setMethod(request, method)

  }

  implicit class ReqHeadersOps[T](headers: RequestHeaders[T]) {
    def withHeader[U](headerName: String)(implicit tupler: Tupler[T, String]): RequestHeaders[tupler.Out] = addHeader(headers, headerName)

    def as[U](implicit header: RequestHeader[U]): RequestHeaders[U] = mapHeader[T, U](headers)
  }




}
