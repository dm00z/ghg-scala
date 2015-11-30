package ghg.pages

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object InfoPage {
  val component = ReactComponentB[Unit]("Info")
    .render(_ => <.div("(info page)"))
    .buildU

  def apply() = component()
}
