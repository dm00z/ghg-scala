package ghg

import ghg.routes.AppRouter
import org.scalajs.dom
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport("GHG")
object Main extends JSApp {
  @JSExport
  def main(): Unit = {
    AppCSS.load()
    AppRouter.router().render(dom.document.getElementById("container"))
  }
}
