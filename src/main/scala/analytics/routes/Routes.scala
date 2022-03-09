package org.narrative
package analytics.routes

import analytics.db.Druid
import analytics.kafka.KafkaProducer
import analytics.model.Model.*
import analytics.routes.QueryMatchers.*

import cats.*
import cats.data.{NonEmptyList, Validated}
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.Status.{BadRequest, InternalServerError, NoContent, Ok}
import org.http4s.circe.*
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*

object Routes {
  def allRoutes[F[_] : Monad]: HttpApp[F] = analyticsRoutes[F].orNotFound

  private def analyticsRoutes[F[_] : Monad]: HttpRoutes[F] =
    val dsl = Http4sDsl[F]
    import dsl.*

    HttpRoutes.of[F] {
      case GET -> Root / "analytics" :? TimestampQueryParamMatcher(maybeTimestamp) =>
        maybeTimestamp.fold(
          parseFailures => BadRequest(s"The request has failed because : ${sanitizedParseFailure(parseFailures)}"),
          timestamp =>
            Druid.analyticsResults(timestamp)
              .fold(
                parseFailure => InternalServerError(s"The request has failed because : ${parseFailure.sanitized}"),
                analytics => Ok(analytics.head.asJson)
              )
        )

      case POST -> Root / "analytics" :? TimestampQueryParamMatcher(maybeTimestamp) +& UserQueryParamMatcher(maybeUserId)
        +& EventQueryParamMatcher(maybeEventType) =>
        (maybeTimestamp, maybeUserId, maybeEventType)
          .mapN(AnalyticsEvent.apply)
          .fold(
            parseFailures => BadRequest(s"The request has failed because : ${sanitizedParseFailure(parseFailures)}"),
            analyticsEvent => {
              KafkaProducer.sendMessage(analyticsEvent.userId, analyticsEvent)
              NoContent()
            }
          )
    }

  private def sanitizedParseFailure[F[_] : Monad](parseFailures: NonEmptyList[ParseFailure]) =
    parseFailures
      .map(_.sanitized)
      .reduce((acc, it) => s"$acc and $it")
}
