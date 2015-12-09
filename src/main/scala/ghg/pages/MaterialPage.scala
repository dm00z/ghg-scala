package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object MaterialPage {
  val component = ReactComponentB[Unit]("Material")
    .render(_ =>
      <.div("(Material page)")
    ).buildU

  def apply(d: ModelProxy[GhgData]) = component()
}
