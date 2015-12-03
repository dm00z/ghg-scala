package ghg.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object AerobicPage {
  val component = ReactComponentB[Unit]("Aerobic")
    .render(_ => <.div("(Aerobic page)"))
    .buildU

  def apply() = component()
}
