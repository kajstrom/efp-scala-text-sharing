name := """efp-scala-text-sharing"""
organization := "fi.kajstrom"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += filters
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
libraryDependencies += "net.debasishg" %% "redisclient" % "3.4"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "fi.kajstrom.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "fi.kajstrom.binders._"
