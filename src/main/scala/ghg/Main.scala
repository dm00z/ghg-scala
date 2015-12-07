package ghg

import ghg.routes.AppRouter
import org.scalajs.dom
import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    AppCSS.load()
    AppRouter.router().render(dom.document.getElementById("container"))
  }
}
