package ghg.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object GasPage {
  val component = ReactComponentB[Unit]("Gas")
    .render(_ => <.div("(gas page)"))
    .buildU

  def apply() = component()
}
