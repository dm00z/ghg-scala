package ghg

import ghg.routes.AppRouter
import org.scalajs.dom.document
import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    AppCSS.load()
    AppRouter.router().render(document.getElementById("app"))
  }
}
