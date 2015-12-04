package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object AerobicPage {
  val component = ReactComponentB[Unit]("Aerobic")
    .render(_ =>
      <.div(
        AppHeader(
          "Tính toán phát thải khí nhà kính trực tiếp",
          Some("Tính toán hệ hiếu khí")),
        "(Aerobic page)")
    ).buildU

  def apply() = component()
}
