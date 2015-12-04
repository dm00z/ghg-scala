package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object ElectricPage {
  val component = ReactComponentB[Unit]("Electric")
    .render(_ =>
      <.div(
        AppHeader(
          "Tính toán phát thải khí nhà kính gián tiếp",
          Some("Phát thải KNK từ tiêu thụ điện năng")),
        "(electric page)"
      )
    ).buildU

  def apply() = component()
}
