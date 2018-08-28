scalaVersion := "2.12.4"

name := "kamon-spm-example"

version := "0.1"

fork in run := true

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "1.1.0",
  "io.kamon" %% "kamon-spm" % "1.1.2",
  "io.kamon" %% "kamon-akka-2.5" % "1.1.0",
  "io.kamon" %% "kamon-system-metrics" % "1.0.0",
  "org.apache.thrift" % "libthrift" % "0.9.2"
)
