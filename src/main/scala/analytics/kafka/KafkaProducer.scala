package org.narrative
package analytics.kafka

import analytics.config.Config
import analytics.config.Config.service
import analytics.model.Model.{AnalyticsEvent, UserId}

import io.circe.generic.auto.*
import io.circe.syntax.*
import org.apache.kafka.clients.admin.{Admin, NewTopic}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties
import scala.concurrent.duration.*
import scala.jdk.CollectionConverters.*

object KafkaProducer {

  val analyticsTopic = new NewTopic(service.kafka.topic.name, service.kafka.topic.numOfPartitions, service.kafka.topic.replication)
  val kafkaAdmin: Admin = Admin.create(service.kafka.properties.asJava)
  val kafkaProducer: KafkaProducer[UserId, String] = new KafkaProducer[UserId, String](service.kafka.properties.asJava)

  kafkaAdmin.createTopics(List(analyticsTopic).asJava)

  def sendMessage(key: UserId, value: AnalyticsEvent) =
    kafkaProducer.send(new ProducerRecord(analyticsTopic.name(), key, value.asJson.toString))

  sys.addShutdownHook(kafkaProducer.flush())
}
