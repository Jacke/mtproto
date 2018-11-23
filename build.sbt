import Dependencies._

resolvers += Resolver.jcenterRepo

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"


resolvers += "third party" at "https://repository.jboss.org/nexus/content/repositories/thirdparty-releases/"

resolvers += "Geotoolkit" at "http://maven.geotoolkit.org/"


lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "com.github.gvolpe",
      scalaVersion := "2.12.3",
      version := "0.1.0-SNAPSHOT",
      scalacOptions := Seq(
        "-deprecation",
        "-encoding",
        "UTF-8",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-Ypartial-unification"
      )
    )
  ),
  name := "cats-effect-demo",
  libraryDependencies ++= Seq(
    catsEffect,
    monixEval,
    fs2,
    http4sServer,
    http4sSClient,
    http4sDsl,
    http4sSCirce,
    circeCore,
    circeGeneric,
    doobieCore,
    doobieH2,
    sttp,
    sttpCats,
    cats,
    scalaTest % Test
  )
)

libraryDependencies += "io.frees" %% "frees-core" % "0.8.2"

libraryDependencies += "io.monix" %% "monix" % "3.0.0-RC1"

libraryDependencies += "com.github.dragon66" % "icafe" % "1.1-SNAPSHOT" % "compile"

libraryDependencies += "ru.tinkoff" %% "typed-schema" % "0.10.4"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.17"

libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "0.11"

libraryDependencies += "co.fs2" %% "fs2-core" % "1.0.0" // For cats 1.4.0 and cats-effect 1.0


addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full)

scalacOptions += "-Xplugin-require:macroparadise"

scalacOptions in (Compile, console) := Seq() 
