package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object AnaerobicPage {
  val component = ReactComponentB[Unit]("Anaerobic")
    .render(_ =>
      <.div(
        AppHeader(
          "Tính toán phát thải khí nhà kính trực tiếp",
          Some("Tính toán hệ yếm khí")),
        "(Anaerobic page)")
    ).buildU

  def apply() = component()
}
