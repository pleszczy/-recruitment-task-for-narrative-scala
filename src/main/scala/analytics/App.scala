package org.narrative
package analytics

import analytics.config.Config.service
import analytics.routes.Routes

import cats.effect.*
import org.http4s.blaze.server.BlazeServerBuilder

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


