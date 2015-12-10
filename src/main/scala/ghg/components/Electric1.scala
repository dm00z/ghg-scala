package ghg.components

import chandu0101.scalajs.react.components.materialui.{MuiTextFieldM, MuiTextField}
import diode.react.ModelProxy
import model.{GhgData, ElectricData}, ElectricData.D1
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import ghg.Utils._

object Electric1 {
  type Props = ModelProxy[GhgData]

  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.electric._1)
  }

  case class Backend($: BackendScope[Props, _]) {
    private var ratioRef: MuiTextFieldM = _

    def render(P: Props) = {
      val my = P.my()
      <.div(
        <.div(
          "Công suất xử lý (m3/ngày): ",
          P().info.power),
        <.div(
          "Khoảng hệ số (kwh/m3): ", D1.Ratio.range.text, " [Ref: Cheng, 2002]"),
        <.div(
          <.label("Chọn hệ số: "),
          MuiTextField(
            style = AppHeader.txtStyle,
            ref = (r: MuiTextFieldM) => if (r != null) {
              r.setValue(my.ratio.toString)
              ratioRef = r
            },
            onChange = (e: ReactEventI) => {
              val valid = e.target.value between D1.Ratio.range
              ratioRef.setErrorText(if (valid) "" else "invalid!")
              Callback.ifTrue(valid, P.my.dispatch(my.copy(ratio = e.target.value.toDouble)))
            }
          )()),
        <.div(
          "Công suất điện (kwwh/ngày): ", P().power
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Electric1")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
