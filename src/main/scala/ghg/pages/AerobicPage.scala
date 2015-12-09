package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object AerobicPage {
  val component = ReactComponentB[Unit]("Aerobic")
    .render(_ =>
      <.div("(Aerobic page)")
    ).buildU

  def apply(d: ModelProxy[GhgData]) = component()
}
