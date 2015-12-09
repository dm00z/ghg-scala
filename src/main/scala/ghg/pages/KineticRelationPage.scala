package ghg.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import model.GhgData

object KineticRelationPage {
  val component = ReactComponentB[Unit]("KineticRelation")
    .render(_ =>
      <.div("(KineticRelation page)")
    ).buildU

  def apply(d: ModelProxy[GhgData]) = component()
}
