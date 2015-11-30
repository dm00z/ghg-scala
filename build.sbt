enablePlugins(ScalaJSPlugin)

name := "ghg"
version := "0.1"
scalaVersion := "2.11.7"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature")

//scalaJSStage in Global := FastOptStage

// creates single js resource file for easy integration in html page
skip in packageJSDependencies := false
// create launcher file ( its search for object extends JSApp , make sure there is only one file)
persistLauncher in Compile := true
persistLauncher in Test := false

// copy  javascript files to js folder,that are generated using fastOptJS/fullOptJS
Seq(fullOptJS, fastOptJS, packageJSDependencies, packageScalaJSLauncher, packageMinifiedJSDependencies)
  .map(scope => crossTarget in (Compile, scope) := file("js"))
artifactPath in (Compile, fastOptJS) <<= (
  crossTarget in (Compile, fastOptJS),
  moduleName in fastOptJS
) { (dir, name) => dir / s"$name-opt.js" }

val vReact = "0.14.3"
val vScalaReact = "0.10.2"
val vScalaCSS = "0.3.1"
val vScalaReactCom = "0.2.0"
libraryDependencies ++= Seq(
  "com.github.chandu0101.scalajs-react-components" %%% "core" % vScalaReactCom,
  "com.github.japgolly.scalacss" %%% "core" % vScalaCSS,
  "com.github.japgolly.scalacss" %%% "ext-react" % vScalaCSS,
  "com.github.japgolly.scalajs-react" %%% "extra" % vScalaReact,
  "com.github.japgolly.scalajs-react" %%% "core" % vScalaReact
)

jsDependencies ++= Seq(
  "org.webjars.bower" % "react" % vReact / "react-with-addons.js"
    minified "react-with-addons.min.js" commonJSName "React",
  "org.webjars.bower" % "react" % vReact / "react-dom.js"
    minified  "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM"
)