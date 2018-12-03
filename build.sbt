import Dependencies._

resolvers += Resolver.jcenterRepo

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Sonatype Public" at "https://oss.sonatype.org/content/groups/public/"

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
    sttpAkka,
    cats,
    scalaTest % Test
  )
)

libraryDependencies += "io.frees" %% "frees-core" % "0.8.2"

libraryDependencies += "io.monix" %% "monix" % "3.0.0-RC1"

libraryDependencies += "com.github.dragon66" % "icafe" % "1.1-SNAPSHOT" % "compile"

libraryDependencies += "ru.tinkoff" %% "typed-schema" % "0.10.4"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.17"

libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "0.13"

libraryDependencies += "co.fs2" %% "fs2-core" % "1.0.0" // For cats 1.4.0 and cats-effect 1.0

libraryDependencies += "org.scodec" %% "scodec-bits" % "1.1.6"

libraryDependencies += "org.scodec" %% "scodec-akka" % "0.3.0"

libraryDependencies += "org.scalaz" %% "scalaz-zio" % "0.4.1"

libraryDependencies += "org.scalaz" %% "scalaz-zio-interop" % "0.4.1"

libraryDependencies += "com.lihaoyi" %% "upickle" % "0.7.1"


libraryDependencies += "org.scodec" %% "scodec-core" % "1.10.3"

libraryDependencies ++= {
  if (scalaBinaryVersion.value startsWith "2.10")
    Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full))
  else Nil
}



addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full)

scalacOptions += "-Xplugin-require:macroparadise"

scalacOptions in (Compile, console) := Seq() 
