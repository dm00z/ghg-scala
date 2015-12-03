package ghg.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object KineticRelationPage {
  val component = ReactComponentB[Unit]("KineticRelation")
    .render(_ => <.div("(KineticRelation page)"))
    .buildU

  def apply() = component()
}
