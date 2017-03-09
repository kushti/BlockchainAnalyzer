name := "BlockAnalyzer"


version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.bitcoinj" % "bitcoinj-core" % "0.14.4",
  "org.mapdb" % "mapdb" % "2.0-beta13",
  "org.apache.commons" % "commons-math3" % "3.6.1"
)