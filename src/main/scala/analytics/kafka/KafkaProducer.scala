package org.narrative
package analytics.kafka

import analytics.Model
import analytics.QueryParameters.UserId

import org.apache.kafka.clients.admin.{Admin, NewTopic}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import analytics.Model.AnalyticsEvent

import java.util.Properties
import scala.concurrent.duration.*
import scala.jdk.CollectionConverters.*
import io.circe.generic.auto.*
import io.circe.syntax.*

object KafkaProducer {
  val adminSettings = Map(
    "bootstrap.servers" -> "localhost:29092"
  )

  val producerSettings = Map(
    "bootstrap.servers" -> "localhost:29092",
    "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "value.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "min.insync.replicas" -> "1", // Just for local setup before pureconfig is added
    "enable.idempotence" -> "true",
    "acks" -> "all",
    "retries" -> Integer.MAX_VALUE.toString,
    "max.in.flight.requests.per.connection" -> "5",
  )

  val analyticsTopic = new NewTopic("analytics.v1", 18, 1.toShort)
  val kafkaAdmin: Admin = Admin.create(adminSettings.asJava)
  val kafkaProducer: KafkaProducer[UserId, String] = new KafkaProducer[UserId, String](producerSettings.asJava)

  kafkaAdmin.createTopics(List(analyticsTopic).asJava)

  def sendMessage(key: UserId, value: AnalyticsEvent) =
    kafkaProducer.send(new ProducerRecord(analyticsTopic.name(), key, value.asJson.toString))

}
