package ghg.pages

import ghg.components.AppHeader
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object KineticRelationPage {
  val component = ReactComponentB[Unit]("KineticRelation")
    .render(_ =>
      <.div(
        AppHeader(
          "Tính toán phát thải khí nhà kính trực tiếp",
          Some("Thông số quan hệ động học của các quá trình")),
      "(KineticRelation page)")
    ).buildU

  def apply() = component()
}
