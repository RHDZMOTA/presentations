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
  )
