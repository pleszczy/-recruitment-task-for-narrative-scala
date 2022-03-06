package org.narrative
package analytics

import analytics.QueryParameters.{EventType, Timestamp, UserId}

object Model {

  final case class AnalyticsEvent(timestamp: Timestamp, userId: UserId, eventType: EventType)

  final case class AnalyticsResults(unique_users: Long, clicks: Long, impressions: Long)

}
