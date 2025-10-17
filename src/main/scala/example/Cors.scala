package example

import zio.http.Header.AccessControlAllowOrigin
import zio.http.Header.Origin
import zio.http.Middleware

object Cors {
  def middleware(config: CorsConfig): Middleware[Any] = {
    val allowedOrigin = config.allowedOrigin
    val zioCorsConfig = Middleware.CorsConfig(
      allowedOrigin = {
        case _ if allowedOrigin == "*" =>
          println("PROCESSING REQUEST WITH *")
          Some(AccessControlAllowOrigin.All)
        case origin                    =>
          println(s"PROCESSING REQUEST WITH ${origin}")
          Origin
            .parse(allowedOrigin)
            .toOption
            .flatMap(a => Option.when(a == origin)(AccessControlAllowOrigin.Specific(origin)))
      },
    )
    Middleware.cors(zioCorsConfig)
  }
}
