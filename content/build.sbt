import sbt._

enablePlugins(TutPlugin)

lazy val content = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.rhdzmota",
      scalaVersion := "2.12.8",
      version      := "0.0.0-SNAPSHOT"
    )),
    name := "content",
    tutSourceDirectory := baseDirectory.value / "docs",
    tutTargetDirectory := baseDirectory.value / ".." / ".microdegree" / "source",
    tutNameFilter := """.*\.(rmd|Rmd)""".r,
    libraryDependencies ++= {
      val configVersion = "1.3.1"
      val scalaTestVersion = "3.0.5"
      val sparkVersion = "2.4.0"
      val jpmmlSparkML  = "1.4.5"
      Seq(
        // Apache Spark
        "org.apache.spark" %% "spark-core"  % sparkVersion,
        "org.apache.spark" %% "spark-sql"   % sparkVersion,
        "org.apache.spark" %% "spark-mllib" % sparkVersion,
        // PMML
        "org.jpmml" % "jpmml-sparkml" % jpmmlSparkML,
        // Config
        "com.typesafe" % "config" % configVersion,
        // Logging
        "ch.qos.logback" % "logback-classic" % "1.2.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
        // Testing
        "org.scalatest" %% "scalatest" % scalaTestVersion
      )
    }
  )
