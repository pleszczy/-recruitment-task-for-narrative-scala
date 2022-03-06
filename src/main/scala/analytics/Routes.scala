package org.narrative
package analytics

import analytics.Model.AnalyticsResults
import analytics.QueryMatchers.{EventQueryParamMatcher, TimestampQueryParamMatcher, UserQueryParamMatcher}
import analytics.QueryParameters.Timestamp

import cats.*
import cats.data.{NonEmptyList, Validated}
import cats.effect.*
import cats.implicits.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.Status.{BadRequest, NoContent, Ok}
import org.http4s.circe.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.headers.*
import org.http4s.implicits.*
import org.http4s.server.*


object Routes {
  def allRoutes[F[_] : Monad]: HttpApp[F] = analyticsRoutes[F].orNotFound

  private def analyticsRoutes[F[_] : Monad]: HttpRoutes[F] =
    val dsl = Http4sDsl[F]
    import dsl.*

    HttpRoutes.of[F] {
      case GET -> Root / "analytics" :? TimestampQueryParamMatcher(maybeTimestamp) =>
        maybeTimestamp.fold(
          parseFailures => BadRequest(s"The request has failed because : ${sanitizedParseFailure(parseFailures)}"),
          year => Ok(AnalyticsResults(42, 69, 666).asJson)
        )

      case POST -> Root / "analytics" :? TimestampQueryParamMatcher(maybeTimestamp) +& UserQueryParamMatcher(maybeUserId) +& EventQueryParamMatcher(maybeEventType) => NoContent()
    }

  private def sanitizedParseFailure[F[_] : Monad](parseFailures: NonEmptyList[ParseFailure]) =
    parseFailures.map(_.sanitized).reduce {
      (acc, it) => s"$acc and $it"
    }
}
