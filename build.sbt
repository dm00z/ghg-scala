name := "ghg"
version := "0.1"
scalaVersion := "2.11.7"

enablePlugins(ScalaJSPlugin)

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature")

//libraryDependencies ++= Seq(
//  "com.github.chandu0101.scalajs-react-components" %%% "core" % "0.2.0"
//)

//scalaJSStage in Global := FastOptStage
skip in packageJSDependencies := false
persistLauncher in Compile := true
persistLauncher in Test := false
