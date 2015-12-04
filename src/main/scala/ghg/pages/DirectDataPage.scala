package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object DirectDataPage {
  val component = ReactComponentB[Unit]("DirectData")
    .render(_ =>
      <.div(
        AppHeader(
          "Tính toán phát thải khí nhà kính trực tiếp",
          Some("Thông số dòng vào")),
        "(DirectData page)")
    ).buildU

  def apply() = component()
}
