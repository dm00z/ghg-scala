package ghg.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object DirectDataPage {
  val component = ReactComponentB[Unit]("DirectData")
    .render(_ => <.div("(DirectData page)"))
    .buildU

  def apply() = component()
}
