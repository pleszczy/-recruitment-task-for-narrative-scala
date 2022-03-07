import sbt._
import sbt.librarymanagement.Configurations.{IntegrationTest, Test}

object Dependencies {

  val unitTest = Seq(
    "org.scalactic" %% "scalactic" % Versions.scalactic % Test,
    "org.scalatest" %% "scalatest" % Versions.scalatest % Test
  )

  val integrationTest = Seq(
    "org.scalactic" %% "scalactic" % Versions.scalactic % IntegrationTest,
    "org.scalatest" %% "scalatest" % Versions.scalatest % IntegrationTest,
    "com.dimafeng" %% "testcontainers-scala-scalatest" % Versions.testcontainers % IntegrationTest,
    "org.scalatra" %% "scalatra" % Versions.scalatra % IntegrationTest
  )

  val util = Seq(
    ("com.github.pureconfig" %% "pureconfig" % Versions.pureconfig).cross(CrossVersion.for3Use2_13)
  )

  val http = Seq(
    "org.http4s" %% "http4s-blaze-server" % Versions.http4sVersion,
    "org.http4s" %% "http4s-circe" % Versions.http4sVersion,
    "org.http4s" %% "http4s-dsl" % Versions.http4sVersion,
    "io.circe" %% "circe-generic" % Versions.circeVersion
  )

  val db = Seq(
    // Couldn't find any scala 3 compatible druid clients.
    // Druid has a rest api so Ill just use http4s-blaze-client to query it.
    "org.http4s" %% "http4s-blaze-client" % Versions.http4sVersion,
  )

  val kafka = Seq(
    ("org.apache.kafka" %% "kafka" % Versions.kafka).cross(CrossVersion.for3Use2_13)
  )

  val logging = Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.10" % Runtime
  )

  object Versions {
    val kafka = "3.1.0"
    val logback = "1.2.10"
    val scalactic = "3.2.9"
    val scalatest = "3.2.9"
    val sprayJson = "1.3.6"
    val testcontainers = "0.40.2"
    val scalatra = "2.8.2"
    val http4sVersion = "1.0.0-M31"
    val circeVersion = "0.15.0-M1"
    val logbackVersion = "1.2.10"
    val pureconfig = "0.17.1"
  }
}