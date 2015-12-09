package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object AnaerobicPage {
  val component = ReactComponentB[Unit]("Anaerobic")
    .render(_ =>
      <.div("(Anaerobic page)")
    ).buildU

  def apply(d: ModelProxy[GhgData]) = component()
}
