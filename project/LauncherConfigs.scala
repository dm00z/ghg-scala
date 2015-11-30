
import org.scalajs.core.tools.io.{FileVirtualTextFile, VirtualTextFile}
import org.scalajs.sbtplugin.ScalaJSPlugin.AutoImport._
import sbt.Keys._
import sbt._

object LauncherConfigs {
  val fastOptMain = Def.taskKey[File]("Generate desktop app main process fastopt bundles")
  val fullOptMain = Def.taskKey[File]("Generate desktop app main process fullopt bundles")

  val main_js = file("main.js")
  val render_js = file("assets/render.js")

  private def concat(to: File, from: File, others: VirtualTextFile*): File = {
    IO.copyFile(from, to)
    others.foreach(f => IO.append(to, f.content))
    to
  }
  @inline implicit def file2VirtualTextFile(f: File): VirtualTextFile = FileVirtualTextFile(f)

  private def optJsMain(optJsTask: TaskKey[Attributed[File]]) = (
    resourceDirectory in Compile,
    optJsTask in Compile,
    scalaJSLauncher in Compile
  ) map { (resource, optJs, launcher) =>
    concat(main_js, resource / "loader.js", optJs.data, launcher.data)
  }

  lazy val desktopMainLauncher = Seq(
    fastOptMain in Compile <<= optJsMain(fastOptJS),
    fullOptMain in Compile <<= optJsMain(fullOptJS)
  )

  val fastOptRender = Def.taskKey[File]("Generate desktop app render process fastopt bundles")
  val fullOptRender = Def.taskKey[File]("Generate desktop app render process fullopt bundles")

  private def optJsRender(optJsTask: TaskKey[Attributed[File]]) = (
    optJsTask in Compile,
    scalaJSLauncher in Compile
  ) map { (optJs, launcher) =>
    concat(render_js, optJs.data, launcher.data)
  }

  lazy val desktopRenderLauncher = Seq(
    fastOptRender in Compile <<= optJsRender(fastOptJS),
    fullOptRender in Compile <<= optJsRender(fullOptJS)
  )
}
