package ghg.pages

import diode.react.ModelProxy
import ghg.components._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object UnstableAerobicPage {
  val component = ReactComponentB[GhgData]("UnstableAerobic")
    .render_P { d =>
      <.div(
        Bien1(d)
      )
    }.build

  def apply(d: ModelProxy[GhgData]) = component(d())
}
