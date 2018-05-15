name := "request-maker"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= {
  val loggingV = "3.9.0"
  val logbackV = "1.2.3"
  val dispatchV = "0.14.0"
  Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % loggingV,
    "ch.qos.logback" % "logback-classic" % logbackV,
    "org.dispatchhttp" %% "dispatch-core" % dispatchV
  )
}
