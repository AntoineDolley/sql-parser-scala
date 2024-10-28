ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.3"

lazy val root = (project in file("."))
  .settings(
    name := "sql-parser",
    libraryDependencies += "com.lihaoyi" %% "fastparse" % "3.1.1",
    libraryDependencies += "com.lihaoyi" %% "utest" % "0.8.4" % "test",
    testFrameworks += new TestFramework("utest.runner.Framework")
  )
