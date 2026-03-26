
name := "gfc-util"

organization := "org.gfccollective"

scalaVersion := "2.13.10"

crossScalaVersions := Seq(scalaVersion.value, "2.12.17")

scalacOptions += "-target:jvm-1.8"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
  "org.gfccollective" %% "gfc-collection" % "1.0.0",
  "commons-codec" % "commons-codec" % "1.15",
  "org.gfccollective" %% "gfc-time" % "1.0.0" % Test,
  "org.scalatest" %% "scalatest" % "3.2.20" % Test,
  "org.scalatestplus" %% "scalacheck-1-15" % "3.2.11.0" % Test,
  "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % Test,
  "org.scalacheck" %% "scalacheck" % "1.17.0" % Test,
  "org.mockito" % "mockito-core" % "4.10.0" % Test
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

Test / publishArtifact := false

pomIncludeRepository := { _ => false }

licenses := Seq("Apache-style" -> url("https://raw.githubusercontent.com/gfc-collective/gfc-util/main/LICENSE"))

homepage := Some(url("https://github.com/gfc-collective/gfc-util"))

pomExtra := (
  <scm>
    <url>https://github.com/gfc-collective/gfc-util.git</url>
    <connection>scm:git:git@github.com:gfc-collective/gfc-util.git</connection>
  </scm>
  <developers>
    <developer>
      <id>gheine</id>
      <name>Gregor Heine</name>
      <url>https://github.com/gheine</url>
    </developer>
    <developer>
      <id>ebowman</id>
      <name>Eric Bowman</name>
      <url>https://github.com/ebowman</url>
    </developer>
    <developer>
      <id>andreyk0</id>
      <name>Andrey Kartashov</name>
      <url>https://github.com/andreyk0</url>
    </developer>
  </developers>
)
