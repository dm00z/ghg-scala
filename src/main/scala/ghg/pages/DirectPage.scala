package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object DirectPage {
  val component = ReactComponentB[Unit]("Direct")
    .render(_ =>
      <.div(
        AppHeader("Tính toán phát thải khí nhà kính trực tiếp"),
        "(direct page)"
      )
    ).buildU

  def apply() = component()
}
