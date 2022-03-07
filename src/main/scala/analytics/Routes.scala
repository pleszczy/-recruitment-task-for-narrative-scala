package org.narrative
package analytics

import analytics.Model.{AnalyticsEvent, AnalyticsResults}
import analytics.QueryMatchers.{EventQueryParamMatcher, TimestampQueryParamMatcher, UserQueryParamMatcher}
import analytics.QueryParameters.Timestamp
import analytics.kafka.KafkaProducer

import cats.*
import cats.data.{NonEmptyList, Validated}
import cats.implicits.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.Status.{BadRequest, NoContent, Ok}
import org.http4s.circe.*
import org.http4s.dsl.*

import java.time.Instant
import scala.util.Try

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

      case POST -> Root / "analytics" :? TimestampQueryParamMatcher(maybeTimestamp) +& UserQueryParamMatcher(maybeUserId)
        +& EventQueryParamMatcher(maybeEventType) =>
        (maybeTimestamp,
          maybeUserId,
          maybeEventType)
          .mapN(AnalyticsEvent.apply)
          .fold(
            parseFailures => BadRequest(s"The request has failed because : ${sanitizedParseFailure(parseFailures)}"),
            analyticsEvent =>
              KafkaProducer.sendMessage(analyticsEvent.userId, analyticsEvent)
              NoContent()
          )
    }

  private def sanitizedParseFailure[F[_] : Monad](parseFailures: NonEmptyList[ParseFailure]) =
    parseFailures.map(_.sanitized).reduce {
      (acc, it) => s"$acc and $it"
    }
}
