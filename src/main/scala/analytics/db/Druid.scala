package org.narrative
package analytics.db

import analytics.model.Model.Timestamp

object Druid {

  //  TODO: Druid is using a variation of HyperLogLog for COUNT(DISTINCT userID). Set useApproximateCountDistinct to "false"
  def query(timestamp: Timestamp): Unit = {
    s"""
       |SELECT
       |SUM("count") FILTER(WHERE eventType = 'click') AS clicks,
       |SUM("count") FILTER(WHERE eventType = 'impression') AS impressions,
       |COUNT(DISTINCT userId) AS unique_users
       |FROM "analytics-v1"
       |WHERE __time >= DATE_TRUNC('hour', MILLIS_TO_TIMESTAMP($timestamp)) - INTERVAL '1' HOUR
       |""".stripMargin
  }

}
