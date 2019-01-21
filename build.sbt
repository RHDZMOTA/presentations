import sbt._
import microsites._

enablePlugins(MicrositesPlugin)

lazy val content  = Project(id="content", base=file("content"))

lazy val site = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.rhdzmota",
      scalaVersion := "2.12.8",
      version      := "0.0.0-SNAPSHOT"
    )),
    name := "site",
    micrositeName := "Presentations",
    micrositeDescription      := "Talks and Presentations",
    micrositeBaseUrl          := "/presentations",
    micrositeDocumentationUrl := "/presentations/docs.html",
    micrositeAuthor           := "rhdzmota",
    micrositeTwitterCreator   := "@rhdzmota",
    micrositeGitterChannel    := true,
    micrositeGitterChannelUrl := "rhdzmota-presentations/community",
    micrositeHomepage         := "https://rhdzmota.github.io/presentations",
    micrositeGithubOwner      := "rhdzmota",
    micrositeGithubRepo       := "presentations",
    micrositeHighlightTheme   := "atom-one-light",
    micrositeHighlightLanguages ++= Seq("haskell", "fsharp", "scala", "python", "java", "bash", "r"),
    micrositeShareOnSocial    := true,
    micrositeCDNDirectives    := CdnDirectives(
      jsList = List(
        "https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.5/MathJax.js?config=TeX-MML-AM_CHTML,https://rhdzmota.github.io/presentations/js/mathjax-config.js"
      )
    ),
    micrositePalette := Map(
      "brand-primary"     -> "#E05236",
      "brand-secondary"   -> "#3F3242",
      "brand-tertiary"    -> "#2D232F",
      "gray-dark"         -> "#49494B",
      "gray"              -> "#7B7B7E",
      "gray-light"        -> "#E5E5E6",
      "gray-lighter"      -> "#F4F3F4",
      "white-color"       -> "#FFFFFF"
    ),
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
