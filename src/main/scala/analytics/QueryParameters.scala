package org.narrative
package analytics

import io.circe.{Decoder, Encoder}
import org.http4s.ParseFailure

import java.time.Instant
import scala.util.Try

object QueryParameters {
  opaque type EventType = "click" | "impression"
  opaque type UserId = String
  opaque type Timestamp = Long

  implicit val encodeTimestamp: Encoder[Timestamp] = Encoder.encodeLong.contramap[Timestamp](it => it)

  implicit val decodeInstant: Decoder[Timestamp] = Decoder.decodeLong.emapTry(it => Timestamp.safe(it).toTry)

  implicit val encodeUserId: Encoder[UserId] = Encoder.encodeString.contramap[UserId](it => it)

  implicit val decodeUserId: Decoder[UserId] = Decoder.decodeString.emapTry(str => UserId.safe(str).toTry)

  implicit val encodeEventType: Encoder[EventType] = Encoder.encodeString.contramap[EventType](it => it)

  implicit val decodeEventType: Decoder[EventType] = Decoder.decodeString.emapTry(str => EventType.safe(str).toTry)

  object EventType {
    def safe(eventType: String): Either[ParseFailure, EventType] = eventType match
      case "click" => Right("click")
      case "impression" => Right("impression")
      case _ => Left(ParseFailure("Given type is invalid", s"Given type $eventType is invalid"))
  }

  object UserId {
    def safe(userId: String): Either[ParseFailure, UserId] = Option(userId)
      .filter(!_.isBlank)
      .toRight(ParseFailure("The given user is invalid", s"The given user $userId is invalid"))
  }

  object Timestamp {
    def safe(timestamp: Long): Either[ParseFailure, Timestamp] = Option(timestamp)
      .filter(_ > 0)
      .toRight(ParseFailure("The given timestamp is invalid", s"The given timestamp $timestamp is invalid"))
  }
}
