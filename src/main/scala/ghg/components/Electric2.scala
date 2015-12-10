package ghg.components

import chandu0101.scalajs.react.components.materialui.{MuiTextField, MuiTextFieldM}
import diode.react.ModelProxy
import ghg.Utils._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.ElectricData.D1
import model.{ElectricData, GhgData}

object Electric2 {
  type Props = ModelProxy[GhgData]

  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.electric._2)
  }

  case class Backend($: BackendScope[Props, _]) {
    def render(P: Props) = {
      val my = P.my()
      <.div(
        "TODO impl Electric2"
      )
    }
  }

  val component = ReactComponentB[Props]("Electric2")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
