name := """rest"""

version := "1.0-SNAPSHOT"

lazy val rest = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "angularjs" % "1.3.4",
  "org.webjars" % "bootstrap" % "3.3.2",
  jdbc,
  anorm,
  cache,
  ws
)


resolvers ++= Seq( "webjars" at "http://webjars.github.com/m2" )