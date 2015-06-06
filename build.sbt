lazy val commonSettings = Seq(
  organization := "net.fgsquad",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.6",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
)

lazy val bootstrap = (project in file("./bootstrap"))
.enablePlugins(
  SbtWeb,
  JavaAppPackaging)
.settings(
  (managedClasspath in Runtime) += (packageBin in Assets).value
)

lazy val website = (project in file("./website"))
.enablePlugins(
  SbtTwirl,
  SbtWeb,
  JavaAppPackaging)
.settings(
  commonSettings,
  Revolver.settings,
  scalariformSettings,
  (managedClasspath in Runtime) += (packageBin in Assets).value,
  Revolver.reStart <<= Revolver.reStart.dependsOn(WebKeys.nodeModules in Assets)
)



lazy val fg2 = (project in file("."))
  .aggregate(bootstrap, website)
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings)

resolvers in Global ++= Seq(
  "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  "Typesafe bintray maven" at "http://dl.bintray.com/typesafe/maven-releases/com/typesafe/"
)

lazy val doobieversion = "0.2.2"

lazy val http4sversion = "0.7.0"

libraryDependencies in website ++= Seq(
"org.http4s"   %% "http4s-dsl"                % http4sversion, 
"org.http4s"   %% "http4s-blazeserver"        % http4sversion, 
"org.http4s"   %% "http4s-json4s"             % http4sversion,     
"org.http4s"   %% "http4s-argonaut"           % http4sversion,   
"org.http4s"   %% "http4s-twirl"              % http4sversion,      
"org.tpolecat" %% "doobie-core"               % doobieversion,
"org.tpolecat" %% "doobie-contrib-postgresql" % doobieversion,
"io.argonaut"  %% "argonaut"                  % "6.1-M6",
"org.slf4j"     % "slf4j-simple"              % "1.7.7"
)
