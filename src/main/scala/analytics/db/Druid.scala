package org.narrative
package analytics.db

import analytics.config.Config.service
import analytics.db.Druid
import analytics.model.Model.{AnalyticsResults, Timestamp}

import cats.implicits.catsSyntaxEither
import io.circe.*
import io.circe.generic.semiauto.*
import io.circe.parser.decode
import org.http4s.ParseFailure
import org.slf4j.LoggerFactory
import scalaj.http.{Http, HttpResponse}

object Druid {
  given fooDecoder: Decoder[AnalyticsResults] = deriveDecoder[AnalyticsResults]

  def analyticsResults(timestamp: Timestamp): Either[ParseFailure, List[AnalyticsResults]] = {
    val queryStr = query(timestamp)
    val response =
      Http(druidUri)
        .header("Content-Type", "application/json")
        .postData(queryStr)
        .asString

    if (response.is2xx) {
      decode[List[AnalyticsResults]](response.body)
        .leftMap(err => ParseFailure(err.getMessage, err.getMessage))
    } else {
      logger.error(s"Failed to query analytics, $response")
      Left(ParseFailure(s"$response", s"$response"))
    }
  }

  private def logger = LoggerFactory.getLogger("Druid")

  private def druidUri = s"${service.druid.schema}://${service.druid.authority}/${service.druid.sqlPath}"

  //  TODO: Druid is using a variation of HyperLogLog for COUNT(DISTINCT userID). Set useApproximateCountDistinct to "false"
  private def query(timestamp: Timestamp): String = {
    val sql: String =
      s"SELECT SUM(\\\"count\\\") FILTER(WHERE eventType = 'click') AS clicks,SUM(\\\"count\\\") FILTER(WHERE eventType = 'impression') AS impressions,COUNT(DISTINCT userId) AS unique_users FROM \\\"analytics-v1\\\" WHERE __time >= DATE_TRUNC('hour', MILLIS_TO_TIMESTAMP($timestamp)) - INTERVAL '1' HOUR"
    s"{\"query\" : \"$sql\"}"
  }
}
