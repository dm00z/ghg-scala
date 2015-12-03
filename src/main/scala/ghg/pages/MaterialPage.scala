package ghg.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object MaterialPage {
  val component = ReactComponentB[Unit]("Material")
    .render(_ => <.div("(Material page)"))
    .buildU

  def apply() = component()
}
