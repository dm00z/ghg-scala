package ghg

import ghg.routes.AppRouter
import org.scalajs.dom
import tex.TeX
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport("GHG")
object Main extends JSApp {
  @JSExport
  def main(): Unit = {
    AppCSS.load()
//    TeX.config()
    AppRouter.router().render(dom.document.getElementById("container"))
  }
}
