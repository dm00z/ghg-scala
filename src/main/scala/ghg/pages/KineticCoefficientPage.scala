package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object KineticCoefficientPage {
  val component = ReactComponentB[Unit]("KineticCoefficient")
    .render(_ =>
      <.div(
        AppHeader(
          "Tính toán phát thải khí nhà kính trực tiếp",
          Some("Hệ số động học của các quá trình")),
        "(KineticCoefficient page)")
    ).buildU

  def apply() = component()
}
