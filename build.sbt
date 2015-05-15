lazy val commonSettings = Seq(
  organization := "net.fgsquad",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.6",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
)

lazy val website = (project in file("./website"))
.enablePlugins(SbtTwirl)
.enablePlugins(SbtWeb)
.enablePlugins(JavaAppPackaging)
.settings(commonSettings)
.settings(Revolver.settings)

lazy val bootstrap = (project in file("./bootstrap"))
.enablePlugins(SbtWeb)
.enablePlugins(JavaAppPackaging)
.settings(commonSettings)

lazy val fg2 = (project in file("."))
.aggregate(bootstrap, website)
.enablePlugins(JavaAppPackaging)
.settings(commonSettings)

resolvers in Global ++= Seq(
  "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  "Typesafe bintray maven" at "http://dl.bintray.com/typesafe/maven-releases/com/typesafe/"
)

lazy val doobieversion = "0.2.2-SNAPSHOT" //publish-local from shapeless 2.2.0 branch; this is needed to not break stuff

lazy val http4sversion = "0.7.0"

libraryDependencies in ThisBuild ++= Seq(
"org.http4s"   %% "http4s-dsl"                % http4sversion, 
"org.http4s"   %% "http4s-blazeserver"        % http4sversion, 
"org.http4s"   %% "http4s-json4s"             % http4sversion,     
"org.http4s"   %% "http4s-argonaut"           % http4sversion,   
"org.http4s"   %% "http4s-twirl"              % http4sversion,      
//"org.tpolecat" %% "doobie-core"               % doobieversion, //hold until doobie 0.2.2 is released to prevent brackage on shapeless
//"org.tpolecat" %% "doobie-contrib-postgresql" % doobieversion, //hold until doobie 0.2.2 is released to prevent brackage on shapeless
"io.argonaut"  %% "argonaut"                  % "6.1-M6",
"org.slf4j"     % "slf4j-simple"              % "1.7.7"
)

//Revolver.settings

//Revolver.reStart <<= Revolver.reStart.dependsOn(WebKeys.assets in Assets in website)

//Revolver.reStart <<= Revolver.reStart.dependsOn(NativePackagerKeys.stage)

stage in fg2 <<= (stage in fg2).dependsOn(stage in website)

WebKeys.packagePrefix in Assets in website := "public/"

(managedClasspath in Runtime) += (packageBin in Assets in website).value

(managedClasspath in Runtime) += (packageBin in Assets in bootstrap).value

scalariformSettings