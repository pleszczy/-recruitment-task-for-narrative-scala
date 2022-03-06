import sbt._
import sbt.librarymanagement.Configurations.{IntegrationTest, Test}

object Dependencies {

  val unitTest = Seq(
    "org.scalatest" %% "scalatest" % Versions.scalatest % Test
  )

  val integrationTest = Seq(
    "org.scalatest" %% "scalatest" % Versions.scalatest % IntegrationTest,
    "com.dimafeng" %% "testcontainers-scala-scalatest" % Versions.testcontainers % IntegrationTest,
    "org.scalatra" %% "scalatra" % Versions.scalatra % IntegrationTest
  )

  val util = Seq(
    "org.scalactic" %% "scalactic" % Versions.scalactic
  )

  val http = Seq(
    "org.http4s" %% "http4s-blaze-server" % Versions.http4sVersion,
    "org.http4s" %% "http4s-circe" % Versions.http4sVersion,
    "org.http4s" %% "http4s-dsl" % Versions.http4sVersion,
    "io.circe" %% "circe-generic" % Versions.circeVersion
  )

  val db = Seq(
  )

  val logging = Seq(
    "ch.qos.logback"  %  "logback-classic"     % "1.2.10"   % Runtime
  )

  object Versions {
    val logback = "1.2.10"
    val scalactic = "3.2.9"
    val scalatest = "3.2.9"
    val sprayJson = "1.3.6"
    val testcontainers = "0.40.2"
    val scalatra = "2.8.2"
    val http4sVersion = "1.0.0-M31"
    val circeVersion = "0.15.0-M1"
    val LogbackVersion = "1.2.10"
  }

}