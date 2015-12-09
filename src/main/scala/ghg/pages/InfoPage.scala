package ghg.pages

import chandu0101.scalajs.react.components.materialui.MuiTextField
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{GhgData, InfoData}
import scala.language.existentials

object InfoPage {
  type Props = ModelProxy[InfoData]

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = <.div(
      <.div(
        MuiTextField(
          hintText = "Loại nước thải": ReactNode,
          onChange = (e: ReactEventI) => Callback { println(e.target.value) }
        )()),
      <.div(
        MuiTextField(
          hintText = "Tên nhà máy": ReactNode,
          onChange = (e: ReactEventI) => Callback { println(e.target.value) }
        )()),
      <.div(
        MuiTextField(
          hintText = "Địa điểm": ReactNode,
          onChange = (e: ReactEventI) => Callback { println(e.target.value) }
        )())
    )
  }

  val component = ReactComponentB[Props]("Info")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d.zoom(_.info))
}
