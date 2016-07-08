scalaVersion := "2.11.8"

name := "kamon-spm-example"

organization := "com.sematext"

version := "0.1"

aspectjSettings

javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "0.6.1",
  "io.kamon" %% "kamon-spm" % "0.6.1",
  "io.kamon" %% "kamon-akka" % "0.6.1",
  "io.kamon" %% "kamon-system-metrics" % "0.6.1",
  "io.kamon" %% "kamon-log-reporter" % "0.6.1"
)
