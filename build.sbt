import sbt.librarymanagement.Configurations.Test

lazy val root = (project in file("."))
  .settings(
    name := "Analytics",
    idePackagePrefix := Some("org.narrative")
  )

ThisBuild / version := "0.0.1-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

resolvers += "confluent" at "https://packages.confluent.io/maven/"

libraryDependencies ++= Dependencies.db
libraryDependencies ++= Dependencies.kafka
libraryDependencies ++= Dependencies.http
libraryDependencies ++= Dependencies.util
libraryDependencies ++= Dependencies.logging
libraryDependencies ++= Dependencies.unitTest

run / javaOptions += s"-Dconfig.file=${sourceDirectory.value}/main/resources/conf/application.conf"
Test / javaOptions += s"-Dconfig.file=${sourceDirectory.value}/test/resources/conf/application.conf"

scalacOptions ++= Seq(
  "-encoding", "UTF-8", // Specify character encoding used by source files.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-explaintypes", // Explain type errors in more detail.
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
  "-language:higherKinds", // Allow higher-kinded types
  "-language:implicitConversions" // Allow definition of implicit functions called views
)