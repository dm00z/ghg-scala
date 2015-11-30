package ghg.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object DirectPage {
  val component = ReactComponentB[Unit]("Direct")
    .render(_ => <.div("(direct page)"))
    .buildU

  def apply() = component()
}
