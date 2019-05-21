package ghg.pages

import diode.react.ModelProxy
import ghg.components.{Bien4, Bien3, Bien2Anaerobic, Bien1}
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object AnaerobicPage {
  val component = ReactComponentB[GhgData]("Anaerobic")
    .render_P { d =>
      <.div(
        Bien1(d),
        Bien2Anaerobic(d),
        Bien3(Bien3.Props(d, false)),
        Bien4(Bien4.Props(d, false))
      )
    }.build

  def apply(d: ModelProxy[GhgData]) = component(d())
}
