package ghg.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object IndirectPage {
  val component = ReactComponentB[Unit]("Indirect")
    .render(_ => <.div("(indirect page)"))
    .buildU

  def apply() = component()

}
