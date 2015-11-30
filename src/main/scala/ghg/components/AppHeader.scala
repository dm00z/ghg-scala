package ghg.components

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object AppHeader {
  val component = ReactComponentB[Unit]("AppHeader")
    .render(_ => <.div("(app header)"))
    .buildU

  @inline def apply() = component()
}
