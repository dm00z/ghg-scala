package ghg.pages

import diode.react.ModelProxy
import ghg.components.{Bien3, Bien2Aerobic, Bien1}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object AerobicPage {
  val component = ReactComponentB[GhgData]("Aerobic")
    .render_P { d =>
      <.div(
        Bien1(d),
        Bien2Aerobic(d),
        Bien3(d)
      )
    }.build

  def apply(d: ModelProxy[GhgData]) = component(d())
}
