package ghg.components

import ghg.Utils._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import model.GhgData
import tex.TeX._

object Bien5 {
  type Props = GhgData

  case class Backend($: BackendScope[Props, _]) {
    def render(d: Props) = {
      val b5 = d.bien5
      <.div(
        <.h3(f"5. Lượng tổng phát thải CO2: ${b5.co2_tongPhatThai}%,.2f g/ngày = ${b5.co2_tongPhatThai/1000}%,.2f kg/ngày = ${b5.co2_tongPhatThai * 356/1000}%,.2f tấn/năm".teX)
      )
    }
  }

  val component = ReactComponentB[Props]("Bien5")
    .renderBackend[Backend]
    .build

  def apply(d: GhgData) = component(d)
}
