package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object GasPage {
  val component = ReactComponentB[Unit]("Gas")
    .render(_ =>
      <.div(
        AppHeader(
          "Tính toán phát thải khí nhà kính gián tiếp",
          Some("Phát thải KNK từ sản xuất và vận chuyển khí tự nhiên")),
        "(gas page)"
      )
    ).buildU

  def apply() = component()
}
