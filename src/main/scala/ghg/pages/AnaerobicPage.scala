package ghg.pages

import diode.react.ModelProxy
import ghg.components.{Bien3, Bien2Anaerobic, Bien1}
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object AnaerobicPage {
  val component = ReactComponentB[GhgData]("Anaerobic")
    .render_P { d =>
      <.div(
        Bien1(d),
        Bien2Anaerobic(d),
        Bien3(d)
      )
    }.build

  def apply(d: ModelProxy[GhgData]) = component(d())
}
