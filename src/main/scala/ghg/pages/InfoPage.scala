package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object InfoPage {
  val component = ReactComponentB[Unit]("Info")
    .render(_ =>
      <.div(
        AppHeader("Thông tin chung"),
        "(info page)"
      )
    ).buildU

  def apply() = component()
}
