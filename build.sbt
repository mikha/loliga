enablePlugins(ScalaJSPlugin)

name := "LOLiga"

scalaVersion := "2.13.4"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

scalaJSStage in Global := FastOptStage

scalaJSUseMainModuleInitializer := true

mainClass in Compile := Some("com.dewdrop.loliga.LoligaApp")

libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.9.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"
