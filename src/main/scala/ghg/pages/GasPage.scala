package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object GasPage {
  val component = ReactComponentB[Unit]("Gas")
    .render(_ =>
      <.div("(gas page)")
    ).buildU

  def apply(d: ModelProxy[GhgData]) = component()
}
