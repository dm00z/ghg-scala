package ghg.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object KineticCoefficientPage {
  val component = ReactComponentB[Unit]("KineticCoefficient")
    .render(_ => <.div("(KineticCoefficient page)"))
    .buildU

  def apply() = component()
}
