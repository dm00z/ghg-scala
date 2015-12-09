package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object KineticCoefficientPage {
  val component = ReactComponentB[Unit]("KineticCoefficient")
    .render(_ =>
      <.div("(KineticCoefficient page)")
    ).buildU

  def apply(d: ModelProxy[GhgData]) = component()
}
