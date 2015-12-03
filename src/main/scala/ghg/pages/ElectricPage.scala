package ghg.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object ElectricPage {
  val component = ReactComponentB[Unit]("Electric")
    .render(_ => <.div("(electric page)"))
    .buildU

  def apply() = component()
}
