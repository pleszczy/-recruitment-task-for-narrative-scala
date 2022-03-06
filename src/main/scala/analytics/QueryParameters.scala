package org.narrative
package analytics

import org.http4s.ParseFailure

object QueryParameters {
  opaque type EventType = "click" | "impression"
  opaque type UserId = String
  opaque type Timestamp = Long

  object EventType {
    def safe(eventType: String): Either[ParseFailure, EventType] = eventType match
      case "click" => Right("click")
      case "impression" => Right("impression")
      case _ => Left(ParseFailure("Given type is invalid", s"Given type $eventType is invalid"))
  }

  object UserId {
    def safe(userId: String): Either[ParseFailure, UserId] = Option(userId)
      .filter(it => it.isBlank)
      .toRight(ParseFailure("The given user is invalid", s"The given user $userId is invalid"))
  }

  object Timestamp {
    def safe(timestamp: Long): Either[ParseFailure, Timestamp] = Option(timestamp)
      .filter(_ > 0)
      .toRight(ParseFailure("The given timestamp is invalid", s"The given timestamp $timestamp is invalid"))
  }
}
