ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .settings(
    name := "zio-http-app-composition-cors-bug",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.14",
      "dev.zio" %% "zio-http" % "3.0.1"
    )
  )
