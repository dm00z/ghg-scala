enablePlugins(ScalaJSPlugin)

name := "ghg"
version := "0.1"
scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature"
  ,"-language:existentials", "-language:higherKinds"
//  ,"-language:postfixOps", "-language:implicitConversions"
)

//scalaJSStage in Global := FastOptStage

// creates single js resource file for easy integration in html page
//skip in packageJSDependencies := false

// create launcher file ( its search for object extends JSApp , make sure there is only one file)
persistLauncher in Compile := true
persistLauncher in Test := false

scalaJSOptimizerOptions in fastOptJS ~= { _.withDisableOptimizer(true) }

// copy  javascript files to js folder,that are generated using fastOptJS/fullOptJS
Seq(fullOptJS, fastOptJS, packageJSDependencies, packageScalaJSLauncher, packageMinifiedJSDependencies)
  .map(scope => crossTarget in (Compile, scope) := file("assets"))
artifactPath in (Compile, fastOptJS) <<= (
  crossTarget in (Compile, fastOptJS),
  moduleName in fastOptJS
) { (dir, name) => dir / s"$name-opt.js" }

//dependencies
val VsReact = "0.10.3"
val VsCss = "0.3.2"
val VsReactCom = "0.3.0"
val VsMoment = "0.1.4"
val Vdiode = "0.3.0"
val Vmonocle = "1.2.0"
libraryDependencies ++= Seq(
  "org.singlespaced" %%% "scalajs-d3" % "0.2.0-SNAPSHOT",
  "com.github.japgolly.fork.monocle" %%% "monocle-macro" % Vmonocle,
  "io.github.widok" %%% "scala-js-momentjs" % VsMoment,
  "me.chrons" %%% "diode-react" % Vdiode,
  "com.github.chandu0101.scalajs-react-components" %%% "core" % VsReactCom,
  "com.github.japgolly.scalacss" %%% "ext-react" % VsCss,
  "com.github.japgolly.scalajs-react" %%% "extra" % VsReact
)
dependencyOverrides ++= Set(
  "com.github.japgolly.scalacss"      %%% "core" % VsCss,
  "com.github.japgolly.scalajs-react" %%% "core" % VsReact
)

addCompilerPlugin(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full))
