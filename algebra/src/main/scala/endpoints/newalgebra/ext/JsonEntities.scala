package endpoints.newalgebra.ext

import endpoints.newalgebra.basic.Endpoints

import scala.language.higherKinds

/**
  * Algebra interface for describing JSON entities in requests and responses.
  *
  * {{{
  *   /**
  *     * Describes an HTTP endpoint whose:
  *     *  - request uses verb “GET”,
  *     *  - URL is made of the segment “/user” followed by a `String` segment,
  *     *  - response content type is JSON and contains a `User`
  *     */
  *   val example = endpoint(get(path / "user" / segment[UUID]), jsonResponse[User])
  * }}}
  */
trait JsonEntities extends Endpoints {

  /** Type class defining how to represent the `A` information as a JSON request entity */
  type JsonRequest[A]

  /** Defines a `RequestEntity[A]` given an implicit `JsonRequest[A]` */
  def jsonRequest[A : JsonRequest]: RequestEntity[A]


  /** Type class defining how to represent the `A` information as a JSON response entity */
  type JsonResponse[A]

  /** Defines a `Response[A]` given an implicit `JsonResponse[A]` */
  def jsonResponse[A : JsonResponse]: Response[A]

}
