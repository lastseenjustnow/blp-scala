import sbt.Keys._
import sbt.file

val akkaVersion = "2.6.5"
val quartzSchedulerVersion = "1.8.3-akka-2.6.x"
val mssqlVersion = "8.2.2.jre11"
val pureConfigVersion = "0.12.3"
val slf4fVersion = "1.7.5"

lazy val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  scalaVersion := "2.12.9",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.enragedginger" %% "akka-quartz-scheduler" % quartzSchedulerVersion,
    "com.microsoft.sqlserver" % "mssql-jdbc" % mssqlVersion,
    "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,
    "org.slf4j" % "slf4j-api" % slf4fVersion,
    "org.slf4j" % "slf4j-log4j12" % slf4fVersion
  )
)

lazy val blp_scala = (project in file("."))
  .aggregate(
    rate
  )

lazy val rate = (project in file("rate"))
  .settings(
    commonSettings,
    mainClass in assembly := Some("rates.SimpleCurrRatesRequestApp"),
    name := "rate"
  )