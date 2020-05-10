import sbt.Keys._
import sbt.file

scalaVersion := "2.13.2"

val akkaVersion = "2.6.5"
val quartzScheduler = "1.8.3-akka-2.6.x"

lazy val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  scalaVersion := "2.13.2",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.enragedginger" %% "akka-quartz-scheduler" % quartzScheduler
  )
)

lazy val blp_scala = (project in file("."))
  .aggregate(
    rate
  )

lazy val rate = (project in file("rate"))
  .settings(
    commonSettings,
    name := "rate"
  )