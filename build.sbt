name := "kafka-connect-satori"

version := "0.1.0"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.satori" % "satori-rtm-sdk-jackson2" % "1.0.3",
  "org.apache.kafka" % "connect-api" % "0.10.2.1" % "provided"
)

assemblyJarName in assembly := s"kafka-connect-satori-${version.value}.jar"

coverageExcludedPackages := """
    |fr.xebia.ldi.satori.*.*Task;
    |fr.xebia.ldi.satori.*.*Connector;
    |fr.xebia.ldi.satori.common.SatoriConstant;
  """.stripMargin