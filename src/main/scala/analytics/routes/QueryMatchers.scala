package org.narrative
package analytics.routes

import analytics.model.Model.{EventType, Timestamp, UserId}

import org.http4s.QueryParamDecoder
import org.http4s.dsl.impl.ValidatingQueryParamDecoderMatcher

object QueryMatchers {
  given eventQueryParamDecoder: QueryParamDecoder[EventType] = QueryParamDecoder[String]
    .emap(EventType.safe)

  given userQueryParamDecoder: QueryParamDecoder[UserId] = QueryParamDecoder[String].emap(UserId.safe)

  given timestampQueryParamDecoder: QueryParamDecoder[Timestamp] = QueryParamDecoder[Long].emap(Timestamp.safe)

  object TimestampQueryParamMatcher extends ValidatingQueryParamDecoderMatcher[Timestamp]("timestamp")

  object UserQueryParamMatcher extends ValidatingQueryParamDecoderMatcher[UserId]("user")

  object EventQueryParamMatcher extends ValidatingQueryParamDecoderMatcher[EventType]("event")
}
