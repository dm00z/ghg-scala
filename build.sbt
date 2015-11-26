name := "ghg"
version := "0.1"
//scalaVersion := "2.11.7"
scalaVersion := "2.12.0-M3"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature"
  ,"-Yinline-warnings" //, "-Yinline"
  ,"-Xfuture" //, "–Xverify", "-Xcheck-null"
//  ,"-Xexperimental"
//  ,"-Yopt:l:classpath"
  ,"-Ywarn-unused-import", "-Ywarn-numeric-widen"
  //,"nullary-unit", "nullary-override", "unsound-match", "adapted-args", "infer-any"
)

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3",
//  "org.controlsfx"  % "controlsfx"  % "8.40.10",
  "org.scalafx"     %% "scalafx"    % "8.0.60-R9"
)

mainClass := Some("ghg.Main")

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))
