package example

import zio.http.*
import zio.ZIO
import zio.ZIOAppDefault

object Main extends ZIOAppDefault {

  case class StrictCorsConfig(allowedOrigin: String) extends CorsConfig
  case class PermissiveCorsConfig(allowedOrigin: String) extends CorsConfig

  // App 1: Strict CORS (localhost:5173)
  val app1 = {
    val corsConfig = StrictCorsConfig("http://localhost:5173")
    println(s"[APP1] Configured with CORS allowedOrigin: ${corsConfig.allowedOrigin}")

    Routes(
      Method.GET / "app1" / "health" -> handler { (req: Request) =>
        println(s"[APP1] Handling request from origin: ${req.header(Header.Origin)}")
        Response.text("App1 OK")
      }
    ) @@ Cors.middleware(corsConfig)
  }

  // App 2: Permissive CORS (*)
  val app2 = {
    val corsConfig = PermissiveCorsConfig("*")
    println(s"[APP2] Configured with CORS allowedOrigin: ${corsConfig.allowedOrigin}")

    Routes(
      Method.GET / "app2" / "health" -> handler { (req: Request) =>
        println(s"[APP2] Handling request from origin: ${req.header(Header.Origin)}")
        Response.text("App2 OK")
      }
    ) @@ Cors.middleware(corsConfig)
  }

  val combinedApp = ZIO.succeed(app1 ++ app2)

  override val run: ZIO[Any, Throwable, Nothing] =
    combinedApp
      .flatMap(_.serve)
      .provide(Server.default)
}
