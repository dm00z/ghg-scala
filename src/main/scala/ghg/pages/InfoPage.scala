package ghg.pages

import chandu0101.scalajs.react.components.materialui.{MuiTextFieldM, MuiTextField}
import diode.react.ModelProxy
import ghg.components.AppHeader
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.{GhgData, InfoData}
import scala.language.existentials
import ghg.Utils._

object InfoPage {
  type Props = ModelProxy[InfoData]

  case class Backend($: BackendScope[Props, _]) {
    private var powerRef: MuiTextFieldM = _

    def unmount(P: Props) = P.dispatch {
      println(powerRef.getValue())
      P.value.copy(power = powerRef.getValue() >| 0)
    }

    def render(P: Props) =
      <.div(
        <.div(
          <.label("Công suất xử lý (m3/ngày): "),
          MuiTextField(
            style = AppHeader.txtStyle,
            ref = (r: MuiTextFieldM) => if (r != null) {
              r.setValue(P().power.toString)
              powerRef = r
            },
            onChange = (e: ReactEventI) => Callback {
              powerRef.setErrorText(if (e.target.value ># 0) "" else "power phải là số > 0")
            })()
        )
      )
  }

  val component = ReactComponentB[Props]("Info")
    .stateless
    .renderBackend[Backend]
    .componentWillUnmount(scope => scope.backend.unmount(scope.props))
    .build

  def apply(d: ModelProxy[GhgData]) = component(d.zoom(_.info))
}
