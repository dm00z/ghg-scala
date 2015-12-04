package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object MaterialPage {
  val component = ReactComponentB[Unit]("Material")
    .render(_ =>
      <.div(
        AppHeader(
          "Tính toán phát thải khí nhà kính gián tiếp",
          Some("Phát thải KNK từ sử dụng hóa chất")),
          "(Material page)"
      )
    ).buildU

  def apply() = component()
}
