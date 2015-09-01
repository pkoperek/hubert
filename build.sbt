// based on: https://github.com/scalatra/scalatra-sbt-prototype/blob/master/build.sbt

lazy val scalatraVersion = "2.3.1"

lazy val root = (project in file(".")).settings(
  name := "hubert",
  version := "2.0",
  scalaVersion := "2.11.6",
  scalacOptions += "-target:jvm-1.8",
  libraryDependencies ++= Seq(
    "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3",
    "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
    "com.typesafe" % "config" % "1.3.0",
    "org.scalatra" %% "scalatra" % scalatraVersion,
    "org.scalatra" %% "scalatra-scalate" % scalatraVersion,
    "org.scalatra" %% "scalatra-specs2" % scalatraVersion % "test",
    "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
    "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime",
    "org.eclipse.jetty" % "jetty-webapp" % "9.2.10.v20150310" % "container",
    "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
    "io.spray" %% "spray-json" % "1.3.2"
  )
).settings(jetty(): _*)
