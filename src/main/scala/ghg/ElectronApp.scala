package ghg

import scala.scalajs.js
import js.Dynamic.{global => g}

object ElectronApp extends js.JSApp {
  val fs = g.require("fs")

  def main(): Unit = {
    fs.readdirSync(".").asInstanceOf[js.Array[String]]
      .foreach(println)
  }
}
