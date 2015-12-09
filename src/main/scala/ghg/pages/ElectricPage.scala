package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object ElectricPage {
  val component = ReactComponentB[Unit]("Electric")
    .render(_ =>
      <.div("(electric page)")
    ).buildU

  def apply(d: ModelProxy[GhgData]) = component()
}
