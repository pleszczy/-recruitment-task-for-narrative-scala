package org.narrative
package analytics.config

import pureconfig.*
import pureconfig.generic.ProductHint

import scala.annotation.nowarn

case class Topic(name: String, numOfPartitions: Int, replication: Short)

case class Api(port: Int, host: String)

case class Kafka(topic: Topic, properties: Map[String, String])

case class Druid(url: String)

case class Service(api: Api, kafka: Kafka, druid: Druid)

object Config {
  @nowarn
  private implicit def hint[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

  val service: Service = ConfigSource.resources(name = "config/application.conf").loadOrThrow[Service]

  // TODO: Automatic Implicit resolution stopped working in scala 3. Remove the reader boilerplate code when pureconfig scala 3 compatible version is released
  // import pureconfig.generic.auto.*
  given apiReader: ConfigReader[Api] = ConfigReader.forProduct2("port", "host")(Api.apply)

  given topicReader: ConfigReader[Topic] = ConfigReader.forProduct3("name", "numOfPartitions", "replication")(Topic.apply)

  given kafkaReader: ConfigReader[Kafka] = ConfigReader.forProduct2("topic", "properties")(Kafka.apply)

  given druidReader: ConfigReader[Druid] = ConfigReader.forProduct1("url")(Druid.apply)

  given serviceConfigReader: ConfigReader[Service] = ConfigReader.forProduct3("api", "kafka", "druid")(Service.apply)
}
