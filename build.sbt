name := """todo"""
organization := "com.learning"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  guice,
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "org.postgresql" % "postgresql" % "42.2.24",
  "com.rallyhealth" %% "weepickle-v1" % "1.6.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % "test"

)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.learning.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.learning.binders._"
