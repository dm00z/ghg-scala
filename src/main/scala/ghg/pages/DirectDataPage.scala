package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object DirectDataPage {
  val component = ReactComponentB[Unit]("DirectData")
    .render(_ =>
      <.div("(DirectData page)")
    ).buildU

  def apply(d: ModelProxy[GhgData]) = component()
}
