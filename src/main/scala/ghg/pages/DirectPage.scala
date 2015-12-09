package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object DirectPage {
  val component = ReactComponentB[Unit]("Direct")
    .render(_ =>
      <.div("(direct page)")
    ).buildU

  def apply(d: ModelProxy[GhgData]) = component()
}
