package org.narrative
package analytics.kafka

import analytics.config.Config
import analytics.config.Config.service
import analytics.model.Model.{AnalyticsEvent, UserId}

import io.circe.generic.auto.*
import io.circe.syntax.*
import org.apache.kafka.clients.admin.{Admin, NewTopic}
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters.*

object KafkaProducer {
  val analyticsTopic = new NewTopic(service.kafka.topic.name, service.kafka.topic.numOfPartitions, service.kafka.topic.replication)
  val kafkaAdmin: Admin = Admin.create(service.kafka.properties.asJava)
  val kafkaProducer: KafkaProducer[UserId, String] = new KafkaProducer[UserId, String](service.kafka.properties.asJava)

  def sendMessage(key: UserId, value: AnalyticsEvent) =
    kafkaProducer.send(new ProducerRecord(analyticsTopic.name(), key, value.asJson.toString), (metadata, exception) =>
      Option(exception) match
        case Some(ex) => logger.error(s"Failed to send $value because ${exception.getMessage}", ex)
        case None => logger.info(s"Successfully send even $value")
    )

  kafkaAdmin.createTopics(List(analyticsTopic).asJava)

  private def logger = LoggerFactory.getLogger("Kafka")

  sys.addShutdownHook(kafkaProducer.flush())
}
