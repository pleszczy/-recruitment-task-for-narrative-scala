package org.narrative
package analytics

import analytics.config.Config.service

import cats.*
import cats.data.{NonEmptyList, Validated}
import cats.effect.*
import cats.implicits.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.Status.{BadRequest, NoContent, Ok}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.circe.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.headers.*
import org.http4s.implicits.*
import org.http4s.server.*

object App extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO]
      .bindHttp(port = service.api.port, host = service.api.host)
      .withHttpApp(Routes.allRoutes)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}


