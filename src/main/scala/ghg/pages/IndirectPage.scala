package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object IndirectPage {
  val component = ReactComponentB[Unit]("Indirect")
    .render(_ =>
      <.div(
        AppHeader("Tính toán phát thải khí nhà kính gián tiếp"),
        "(indirect page)"
      )
    ).buildU

  def apply() = component()
}
