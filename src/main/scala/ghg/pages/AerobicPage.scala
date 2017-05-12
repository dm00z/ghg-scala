package ghg.pages

import diode.react.ModelProxy
import ghg.components.{Bien3, Bien2Aerobic, Bien1, Bien4, Bien5, Bien6}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object AerobicPage {
  val component = ReactComponentB[GhgData]("Aerobic")
    .render_P { d =>
      <.div(
        Bien1(d),
        Bien2Aerobic(d),
        Bien3(Bien3.Props(d, true)),
        Bien4(d),
        Bien5(d),
        Bien6(d)
      )
    }.build

  def apply(d: ModelProxy[GhgData]) = component(d())
}
