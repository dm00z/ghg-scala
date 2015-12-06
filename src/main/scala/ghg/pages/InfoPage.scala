package ghg.pages

import chandu0101.scalajs.react.components.materialui.MuiTextField
import ghg.components.AppHeader
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object InfoPage {
  case class State(f: AppHeader.Factory)

  val component = ReactComponentB[Unit]("Info")
    .initialState(State(AppHeader.Factory("", "", "")))
    .render(_ =>
      <.div(
        AppHeader("Thông tin chung"),
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
    ).buildU

  def apply() = component()
}
