package ghg

import japgolly.scalajs.react.{ReactDOM, ReactComponentB}
import org.scalajs.dom.document
import japgolly.scalajs.react.vdom.prefix_<^._
import scala.scalajs.js
import js.Dynamic.{global => g}

object ElectronApp extends js.JSApp {
  val fs = g.require("fs")

  def main(): Unit = {
    val HelloMessage = ReactComponentB[String]("HelloMessage")
      .render($ => <.div("Hello ", $.props))
      .build

    ReactDOM.render(HelloMessage("John"), document.getElementById("app"))

    fs.readdirSync(".").asInstanceOf[js.Array[String]]
      .foreach(println)
  }
}
