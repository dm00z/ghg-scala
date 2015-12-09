package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object IndirectPage {
  val component = ReactComponentB[Unit]("Indirect")
    .render(_ =>
      <.div("(indirect page)")
    ).buildU

  def apply(d: ModelProxy[GhgData]) = component()
}
