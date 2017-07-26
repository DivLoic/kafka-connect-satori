name := "kafka-connect-satori"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.satori" % "satori-rtm-sdk-jackson2" % "1.0.3",
  "org.apache.kafka" % "connect-api" % "0.10.2.1" % "provided",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

assemblyJarName in assembly := s"kafka-connect-satori-${version.value}-assembly.jar"
