package ghg.pages

import chandu0101.scalajs.react.components.materialui.{MuiDropDownMenuItem, MuiDropDownMenu}
import diode.react.ModelProxy
import ghg.components.{Electric1, Electric2, Electric3}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import model.ElectricData.CalcMethod
import model.GhgData
import scala.scalajs.js, js.JSConverters._

object ElectricPage {
  type Props = ModelProxy[GhgData]
  implicit final class PropsEx(val p: Props) extends AnyVal {
    @inline def my = p.zoom(_.indirect.electric)
  }

  final case class Backend($: BackendScope[Props, _]) {
    @inline def calcMethods = CalcMethod.values.map(m => MuiDropDownMenuItem(m.id.toString, m.toString)).toJSArray

    def render(P: Props) = {
      val my = P.my()
      <.div(
        <.div(
          <.label("Cách tính công suất tiêu thụ điện năng: "),
          MuiDropDownMenu(
            menuItems = calcMethods,
            selectedIndex = my.method.id,
            onChange = (_: ReactEventI, i: Int, _: js.Any) => P.dispatch(CalcMethod(i))
          )(),
          my.method match {
            case CalcMethod.Method1 => Electric1(P)
            case CalcMethod.Method2 => Electric2(P)
            case CalcMethod.Method3 => Electric3(P)
          }
        )
      )
    }
  }

  val component = ReactComponentB[Props]("Electric")
    .renderBackend[Backend]
    .build

  def apply(d: ModelProxy[GhgData]) = component(d)
}
